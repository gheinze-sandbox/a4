package com.accounted4.assetmgr.core;

import java.util.List;

/**
 *
 */
public interface PartyRepository {
 
    /**
     * Save the party represented by the form bean to the backing repository.
     * 
     * @param partyForm The form bean backing the "party" page containing
     * all the fields required to save the record to the database.
     */
    void save(PartyForm partyForm);
    
    
    void update(PartyForm partyForm);
    
    void deleteParty(long id);
    
    /**
     * Find a "party" based on the id. The party is retrieved whether it
     * is inactive or not.
     * 
     * @param id
     * @return 
     */
    PartyForm getPartyById(long id);
    
    /**
     * Find the "party based on the party's name. The party is retrieved whether it
     * is inactive or not.
     * 
     * @param partyName
     * @return 
     */
    PartyForm getPartyByKey(String partyName);
    
    
    List<PartyForm> findParties(PartyForm partyFormTemplate);

}
