package com.accounted4.assetmgr.core.party;

import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.core.address.AddressForm;
import com.accounted4.assetmgr.support.web.Layout;
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
    private static final String ADDRESS_FORM_BEAN_NAME = "addressForm";
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party")
    public String getPartyPage(Model model) {
        model.addAttribute(FORM_BEAN_NAME, new PartyForm());
        model.addAttribute(ADDRESS_FORM_BEAN_NAME, new AddressForm());
        return ViewRoute.PARTY;
    }
    
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/partyAddress", method = RequestMethod.POST, params="saveAddressAction")
    public ModelAndView savePartyAddress(
            @ModelAttribute PartyForm partyForm
            ,@Valid @ModelAttribute AddressForm addressForm
            ,Errors errors) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        
        if (!errors.hasErrors()) {
            partyService.addAddressToParty(partyForm, addressForm);
            PartyForm retrievedPartyForm = partyService.getPartyById(partyForm.getRecord().getId());
            mav.addObject(FORM_BEAN_NAME, retrievedPartyForm);
            mav.addObject(ADDRESS_FORM_BEAN_NAME, new AddressForm());
        }
        
        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party/{id}")
    public String getParty(Model model, @PathVariable long id) {
        PartyForm party = partyService.getPartyById(id);
        model.addAttribute(FORM_BEAN_NAME, party);
        model.addAttribute(ADDRESS_FORM_BEAN_NAME, new AddressForm());
        return ViewRoute.PARTY;
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="saveAction")
    public ModelAndView saveParty(@Valid @ModelAttribute PartyForm partyForm, Errors errors) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        
        if (!errors.hasErrors()) {
            partyService.saveParty(partyForm);
            PartyForm retrievedPartyForm = partyService.getPartyByName(partyForm.getPartyName());
            mav.addObject(FORM_BEAN_NAME, retrievedPartyForm);
            mav.addObject(ADDRESS_FORM_BEAN_NAME, new AddressForm());
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
            mav.addObject(ADDRESS_FORM_BEAN_NAME, new AddressForm());
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
            mav.addObject(ADDRESS_FORM_BEAN_NAME, new AddressForm());
        mav.addObject("notification", partyForm.getPartyName() + " has been Inactivated.");

        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="clearAction")
    public ModelAndView clearParty(@Valid @ModelAttribute PartyForm partyForm) {
        
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        mav.addObject(FORM_BEAN_NAME, new PartyForm());
        mav.addObject(ADDRESS_FORM_BEAN_NAME, new AddressForm());

        return mav;
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "core/party", method = RequestMethod.POST, params="findAction")
    public ModelAndView findParty(@ModelAttribute PartyForm partyForm, BindingResult bindingResult) {
        
        List<PartyForm> parties = partyService.findParties(partyForm);
        
        if (parties.isEmpty()) {
            bindingResult.rejectValue("partyName", "find.error", "No parties found matching search criteria.");
            ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
            return mav;
        }

        
        ModelAndView mav = new ModelAndView(ViewRoute.SELECT_LIST);
        mav.addObject("selectList", partyService.generateUrlList(parties));
        mav.addObject(ADDRESS_FORM_BEAN_NAME, new AddressForm());
        
        return mav;
        
    }
        
    
    

}
