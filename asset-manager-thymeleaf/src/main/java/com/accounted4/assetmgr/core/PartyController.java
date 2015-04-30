package com.accounted4.assetmgr.core;

import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.support.web.Layout;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Controller
public class PartyController {
    
    @Autowired PartyService partyService;
    
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party")
    public String getPartyPage(Model model) {
        model.addAttribute(new PartyForm());
        return ViewRoute.PARTY;
    }
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="saveAction")
    public ModelAndView saveParty(@Valid @ModelAttribute PartyForm partyForm, Errors errors, Principal principal) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        
        if (!errors.hasErrors()) {
            partyService.saveParty(partyForm);
            PartyForm retrievedPartyForm = partyService.getPartyByKey(partyForm.getPartyName());
            mav.addObject(retrievedPartyForm);
        }
        
        return mav;
        
    }

    
    // TODO: handling other posts: find, update, delete, ... (this is just a template method)
    
    @RequestMapping(value = "core/party", method = RequestMethod.POST)
    public String handlePartyPost(@Valid @ModelAttribute PartyForm partyForm,  @RequestParam String action, Errors errors) {
        
        if (errors.hasErrors()) {
            return ViewRoute.PARTY;
        }
        
        return ViewRoute.PARTY;
    }
        
    
}
