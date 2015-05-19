package com.accounted4.assetmgr.core;

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

    @Autowired
    PartyRepository partyRepository;
    
    
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
        partyRepository.deleteParty(partyForm.getRecord().getId());
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

    @Override
    public List<SelectItem> generateUrlList(List<PartyForm> parties) {
        List<SelectItem> result = new ArrayList<>();
        parties
                .stream()
                .map( (partyForm)-> new SelectItem("/core/party/" + partyForm.getRecord().getId(), partyForm.getPartyName()) )
                .forEach((item) -> { result.add(item); })
                ;
        return result;
    }
    
}
