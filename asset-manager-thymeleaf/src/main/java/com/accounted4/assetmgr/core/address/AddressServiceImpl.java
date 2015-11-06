package com.accounted4.assetmgr.core.address;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

/**
 *
 * @author gheinze
 */
@Component
public class AddressServiceImpl implements AddressService {

    @Autowired AddressRepository addressRepository;
    
    // TODO: inject
    ObjectMapper objectMapper = new ObjectMapper();
    
    
    @Override
    public void updateAddress(AddressForm addressForm) {
        addressRepository.update(addressForm);
    }

    @Override
    public String getAddressList(String filter) {
        
        List<SelectListEntry> resultSet = new ArrayList<>();
        List<AddressForm> addressList = addressRepository.getAddressList(filter);

        for (AddressForm address : addressList) {
            String key = String.valueOf(address.getRecordMetaData().getId());
            String displayValue = address.toString();
            resultSet.add(new SelectListEntry(key, displayValue));
        }

        String result = "";
        
        try {
            result = objectMapper.writeValueAsString(resultSet);
        } catch (IOException ex) {
            throw new DataAccessException("Failure converting an address list to json format", ex) {};
        }
        
        return result;
        
    }

    
    private static class SelectListEntry {
        
        @Getter private final String key;
        @Getter private final String displayValue;
        
       public SelectListEntry(String key, String displayValue) {
           this.key = key;
           this.displayValue = displayValue;
       }
       
    }
    
    
}
