package com.accounted4.assetmgr.core;

import java.util.List;

/**
 *
 * @author gheinze
 */
public interface PartyService {
    
    void saveParty(PartyForm partyForm);
    
    void updateParty(PartyForm partyForm);

    PartyForm getPartyById(long id);
    
    PartyForm getPartyByName(String partyName);
    
    List<PartyForm> findParties(PartyForm partyFormTemplate);

    List<SelectItem> generateUrlList(List<PartyForm> parties);

}
