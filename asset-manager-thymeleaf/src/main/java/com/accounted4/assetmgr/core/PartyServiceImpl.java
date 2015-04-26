package com.accounted4.assetmgr.core;

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
    
}
