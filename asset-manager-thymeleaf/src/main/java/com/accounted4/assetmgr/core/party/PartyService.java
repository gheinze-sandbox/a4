package com.accounted4.assetmgr.core.party;

import com.accounted4.assetmgr.core.SelectItem;
import com.accounted4.assetmgr.core.address.AddressForm;
import java.util.List;

/**
 *
 * @author gheinze
 */
public interface PartyService {
    
    void saveParty(PartyForm partyForm);
    
    void updateParty(PartyForm partyForm);

    void deleteParty(PartyForm partyForm);

    PartyForm getPartyById(long id);
    
    PartyForm getPartyByName(String partyName);
    
    List<PartyForm> findParties(PartyForm partyFormTemplate);

    List<SelectItem> generateUrlList(List<PartyForm> parties);

    void addAddressToParty(PartyForm partyForm, AddressForm addressForm);

    void removeAddressFromParty(PartyForm partyForm, long selectedAddressId);

}
