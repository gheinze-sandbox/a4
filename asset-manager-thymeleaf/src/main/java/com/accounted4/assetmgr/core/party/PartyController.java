package com.accounted4.assetmgr.core.party;

import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.core.ConsumerServiceWrapper;
import com.accounted4.assetmgr.core.RecordMetaData;
import com.accounted4.assetmgr.core.address.AddressForm;
import com.accounted4.assetmgr.core.address.AddressService;
import com.accounted4.assetmgr.support.web.Layout;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * Handle "core/party" url requests which typically involve manipulation of
 * contact information. Note that the PartyForm model will persist through the 
 * session.
 */
@Controller
@RequestMapping(value = "/core/party")
@SessionAttributes("partyForm")
public class PartyController {
    
    @Autowired PartyService partyService;
    @Autowired AddressService addressService;
    
    
    private static final String PARTY_MODEL_NAME = "partyForm";
    private static final String ADDRESS_MODEL_NAME = "addressForm";
    


    // ====================================
    // Party queries
    // ====================================
    
    /**
     * The default `GET` request for a party routes to an empty Party
     * entry form with a model holding two backing form objects:
     * 
     *  * {@link PartyForm} for gathering party details when saving a new Party through form submission
     *  * {@link AddressForm} for gathering address information that can be used to attach an Address to a Party
     * 
     * @return A {@link ModelAndView} for routing to a new Party entry form backed by a {@link PartyForm} and 
     * and {@link AddressForm}
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.GET)
    
    public ModelAndView getPartyEntryForm() {
        return getPartyModelAndView();
    }
    
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "{partyId}", method = RequestMethod.GET)
    
    public ModelAndView getPopulatedPartyEntryForm(@PathVariable long partyId) {
        PartyForm party = partyService.getPartyById(partyId);
        return getPartyModelAndView(party);
    }

    
    /**
     * Route to a list of urls of Parties with names matching the
     * search template. In particular, a case-insensitive search of the Party name.
     * @param partySearchTemplate template upon which the search is based
     * @param bindingResult Form validation object to which error messages can be attached
     * @return a route to a page based on a selection list of Parties (urls)
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="findAction")
    
    public ModelAndView findParty(@ModelAttribute PartyForm partySearchTemplate, BindingResult bindingResult) {
        
        List<PartyForm> parties = partyService.findParties(partySearchTemplate);
        
        if (parties.isEmpty()) {
            bindingResult.rejectValue("partyName", "find.error", "No parties found matching search criteria.");
            return getPartyModelAndView();
        }

        
        ModelAndView mav = new ModelAndView(ViewRoute.SELECT_LIST);
        mav.addObject("selectList", partyService.generateUrlList(parties));
        mav.addObject(ADDRESS_MODEL_NAME, new AddressForm());
        
        return mav;
        
    }
        
    

    // ====================================
    // Party Persisitence
    // ====================================
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="saveAction")
    
    public ModelAndView saveParty(@Valid @ModelAttribute PartyForm party, Errors formValidationErrors) {
        return persist(party, formValidationErrors, partyService.getWrappedSaveService());
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="updateAction")
    
    public ModelAndView updateParty(@Valid @ModelAttribute PartyForm party, Errors formValidationErrors) {
        return persist(party, formValidationErrors, partyService.getWrappedUpdateService());
    }


    /*
     * Save or Update a Party, routing back to the Party entry form with a pre-poplulated party.
    */
    private ModelAndView persist(PartyForm party, Errors formValidationErrors, ConsumerServiceWrapper actionWrapper) {
        
        if (formValidationErrors.hasErrors()) {
            return new ModelAndView(ViewRoute.PARTY);
        }
        
        actionWrapper.getServiceAction().accept(party);
        
        PartyForm retrievedPartyForm = partyService.getPartyByName(party.getPartyName());
        
        ModelAndView mav =  getPartyModelAndView(retrievedPartyForm);
        String notificationMessage = String.format("%s has been %s.", retrievedPartyForm.getPartyName(), actionWrapper.getCompletionMessage());
        addNotificationMessageToMav(mav, notificationMessage);
        
        return mav;
        
    }
    

    // TODO: Delete should delete and inactivate should be a separate call
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="deleteAction")
    
    public ModelAndView deleteParty(@ModelAttribute PartyForm partyForm) {
        partyService.inactivateParty(partyForm);
        ModelAndView mav = getPartyModelAndView();
        String notificationMessage = String.format("%s has been inactivated.", partyForm.getPartyName());
        addNotificationMessageToMav(mav, notificationMessage);
        return mav;
    }

    
    /**
     * Same effect as GETting a new entry form, but handled via a POST.
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="clearAction")
    
    public ModelAndView clearParty() {
        return getPartyModelAndView();
    }

    
    // ====================================
    // Party Address 
    // ====================================
    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "address", method = RequestMethod.POST, params="saveAddressAction")
    
    public ModelAndView savePartyAddress(
            @ModelAttribute PartyForm partyForm
            ,@ModelAttribute @Valid  AddressForm addressForm
            ,RecordMetaData addressRecordMetaData
            ,Errors errors) {
        
        if (errors.hasErrors()) {
            return new ModelAndView(ViewRoute.PARTY);
        }
        
        if (addressRecordMetaData.isEditMode()) {
            addressForm.setRecordMetaData(addressRecordMetaData);
            addressService.updateAddress(addressForm);
        } else {
            partyService.addAddressToParty(partyForm, addressForm);
        }
        
        PartyForm retrievedPartyForm = partyService.getPartyById(partyForm.getRecordMetaData().getId());
        
        return getPartyModelAndView(retrievedPartyForm);
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "addressDelete", method = RequestMethod.POST)
    
    public ModelAndView removePartyAddressMapping(
            @ModelAttribute PartyForm partyForm,
            @RequestParam long selectedAddressId) {

        partyService.removeAddressFromParty(partyForm, selectedAddressId);
        PartyForm retrievedPartyForm = partyService.getPartyById(partyForm.getRecordMetaData().getId());
        
        return getPartyModelAndView(retrievedPartyForm);
        
    }

    
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "addressAttach", method = RequestMethod.POST)
    
    public ModelAndView attachAddress(
            @ModelAttribute PartyForm partyForm,
            @RequestParam long addressId) {

        partyService.attachAddressToParty(partyForm, addressId);
        PartyForm retrievedPartyForm = partyService.getPartyById(partyForm.getRecordMetaData().getId());

        return getPartyModelAndView(retrievedPartyForm);

    }

    
    
    // ====================================
    // Helper methods for returning a ModelAndView response 
    // ====================================
    
    private ModelAndView getPartyModelAndView() {
        return getPartyModelAndView(new PartyForm());
    }
    
    private ModelAndView getPartyModelAndView(PartyForm partyForm) {
        return getPartyModelAndView(partyForm, new AddressForm());
    }

    private ModelAndView getPartyModelAndView(PartyForm partyForm, AddressForm addressForm) {
        ModelAndView mav = new ModelAndView(ViewRoute.PARTY);
        mav.addObject(PARTY_MODEL_NAME, partyForm);
        mav.addObject(ADDRESS_MODEL_NAME, addressForm);
        return mav;
    }

    
    private static final String NOTIFICATION_MSG = "notification";
    
    private void addNotificationMessageToMav(ModelAndView mav, String message) {
         mav.addObject(NOTIFICATION_MSG, message);
    }

}
