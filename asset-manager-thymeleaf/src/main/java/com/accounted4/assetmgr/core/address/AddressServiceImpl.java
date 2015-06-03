package com.accounted4.assetmgr.core.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author gheinze
 */
@Component
public class AddressServiceImpl implements AddressService {

    @Autowired AddressRepository addressRepository;
    
    @Override
    public void updateAddress(AddressForm addressForm) {
        addressRepository.update(addressForm);
    }
    
}
