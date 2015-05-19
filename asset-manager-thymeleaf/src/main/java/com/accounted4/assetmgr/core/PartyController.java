package com.accounted4.assetmgr.core;

import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.support.web.Layout;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Controller
@SessionAttributes("partyForm")
public class PartyController {
    
    @Autowired PartyService partyService;
    
    private static final String FORM_BEAN_NAME = "partyForm";
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party")
    public String getPartyPage(Model model) {
        model.addAttribute(FORM_BEAN_NAME, new PartyForm());
        return ViewRoute.PARTY;
    }
    
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party/{id}")
    public String getParty(Model model, @PathVariable long id) {
        PartyForm party = partyService.getPartyById(id);
        model.addAttribute(FORM_BEAN_NAME, party);
        return ViewRoute.PARTY;
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="saveAction")
    public ModelAndView saveParty(@Valid @ModelAttribute PartyForm partyForm, Errors errors, Principal principal) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        
        if (!errors.hasErrors()) {
            partyService.saveParty(partyForm);
            PartyForm retrievedPartyForm = partyService.getPartyByName(partyForm.getPartyName());
            mav.addObject(FORM_BEAN_NAME, retrievedPartyForm);
        }
        
        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="updateAction")
    public ModelAndView updateParty(@Valid @ModelAttribute PartyForm partyForm, Errors errors) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        
        if (!errors.hasErrors()) {
            partyService.updateParty(partyForm);
            PartyForm retrievedPartyForm = partyService.getPartyById(partyForm.getRecord().getId());
            mav.addObject(FORM_BEAN_NAME, retrievedPartyForm);
            // TODO: move message to a status bar component? Change this to a notify function?
            mav.addObject("notification", retrievedPartyForm.getPartyName() + " has been Updated.");
        }
        
        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="deleteAction")
    public ModelAndView deleteParty(@Valid @ModelAttribute PartyForm partyForm) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        partyService.deleteParty(partyForm);
        mav.addObject(FORM_BEAN_NAME, new PartyForm());
        mav.addObject("notification", partyForm.getPartyName() + " has been Inactivated.");

        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="clearAction")
    public ModelAndView clearParty(@Valid @ModelAttribute PartyForm partyForm) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        mav.addObject(FORM_BEAN_NAME, new PartyForm());

        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="findAction")
    public ModelAndView findParty(@ModelAttribute PartyForm partyForm, BindingResult bindingResult, Principal principal) {
        
        List<PartyForm> parties = partyService.findParties(partyForm);
        
        if (parties.isEmpty()) {
            bindingResult.rejectValue("partyName", "find.error", "No parties found matching search criteria.");
            ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
            return mav;
        }

        
        ModelAndView mav = new ModelAndView(ViewRoute.SELECT_LIST);
        mav.addObject("selectList", partyService.generateUrlList(parties));
        
        return mav;
        
    }
        
    
}
