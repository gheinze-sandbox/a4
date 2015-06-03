package com.accounted4.assetmgr.core.party;

import com.accounted4.assetmgr.core.SelectItem;
import com.accounted4.assetmgr.core.address.AddressForm;
import com.accounted4.assetmgr.core.address.AddressRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author gheinze
 */
@Service
public class PartyServiceImpl implements PartyService {

    @Autowired PartyRepository partyRepository;
    @Autowired AddressRepository addressRepository;
    
    
    @Override
    public void saveParty(PartyForm partyForm) {
        partyRepository.save(partyForm);
    }

    @Override
    public void updateParty(PartyForm partyForm) {
        partyRepository.update(partyForm);
    }

    @Override
    public void deleteParty(PartyForm partyForm) {
        partyRepository.deleteParty(partyForm.getRecordMetaData().getId());
    }

    @Override
    public PartyForm getPartyById(long id) {
        PartyForm result = partyRepository.getPartyById(id);
        List<AddressForm> addressesForParty = addressRepository.getAddressesForParty(id);
        result.setAddresses(addressesForParty);
        return result;
    }

    @Override
    public PartyForm getPartyByName(String partyName) {
        PartyForm result = partyRepository.getPartyByKey(partyName);
        List<AddressForm> addressesForParty = addressRepository.getAddressesForParty(result.getRecordMetaData().getId());
        result.setAddresses(addressesForParty);
        return result;
    }

    @Override
    public List<PartyForm> findParties(PartyForm partyFormTemplate) {
        return partyRepository.findParties(partyFormTemplate);
    }

    @Override
    public List<SelectItem> generateUrlList(List<PartyForm> parties) {
        List<SelectItem> result = new ArrayList<>();
        parties
                .stream()
                .map( (partyForm)-> new SelectItem("/core/party/" + partyForm.getRecordMetaData().getId(), partyForm.getPartyName()) )
                .forEach((item) -> { result.add(item); })
                ;
        return result;
    }

    @Override
    public void addAddressToParty(PartyForm partyForm, AddressForm addressForm) {
        long addressId = addressRepository.save(addressForm);
        partyRepository.addAddressToParty(partyForm, addressId);
    }

    @Override
    public void removeAddressFromParty(PartyForm partyForm, long selectedAddressId) {
        partyRepository.removeAddressFromParty(partyForm, selectedAddressId);
    }
    
}
