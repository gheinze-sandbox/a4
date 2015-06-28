package com.accounted4.assetmgr.core.party;

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
     * @return The id of the newly created Party
     */
    long save(PartyForm partyForm);
    
    
    /**
     * 
     * @param partyForm
     * @return The number of rows affected.
     */
    int update(PartyForm partyForm);
    
    /**
     * 
     * @param id The id of the Party to delete.
     * @return The number of rows affected.
     */
    int inactivateParty(long id);
    
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
    
    /**
     * Retrieves a list of partially populated Party objects. Mapped entries
     * such as lists of addresses, phone numbers, email, etc are not populated. They
     * are populated when a Party is retrieved individually (i.e. getPartyById, getPartyByKey).
     * @param partyFormTemplate
     * @return 
     */
    List<PartyForm> findParties(PartyForm partyFormTemplate);

    void detachAddressFromParty(PartyForm partyForm, long addressId);

    void attachAddressToParty(PartyForm partyForm, long addressId);

}
