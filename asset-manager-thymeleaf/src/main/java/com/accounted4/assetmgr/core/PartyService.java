package com.accounted4.assetmgr.core;

/**
 *
 * @author gheinze
 */
public interface PartyService {
    
    void saveParty(PartyForm partyForm);
    
    PartyForm getPartyById(long id);
    
    PartyForm getPartyByKey(String partyName);

}
