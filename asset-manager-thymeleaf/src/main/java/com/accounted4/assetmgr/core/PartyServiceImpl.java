package com.accounted4.assetmgr.core;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author gheinze
 */
@Service
public class PartyServiceImpl implements PartyService {

    @Autowired
    PartyRepository partyRepository;
    
    
    @Override
    public void saveParty(PartyForm partyForm) {
        partyRepository.save(partyForm);
    }

    @Override
    public PartyForm getPartyById(long id) {
        return partyRepository.getPartyById(id);
    }

    @Override
    public PartyForm getPartyByName(String partyName) {
        return partyRepository.getPartyByKey(partyName);
    }

    @Override
    public List<PartyForm> findParties(PartyForm partyFormTemplate) {
        return partyRepository.findParties(partyFormTemplate);
    }
    
}
