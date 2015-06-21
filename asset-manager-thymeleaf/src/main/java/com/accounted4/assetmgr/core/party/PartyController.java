package com.accounted4.assetmgr.core.party;

import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.core.address.AddressForm;
import com.accounted4.assetmgr.core.address.AddressService;
import com.accounted4.assetmgr.support.web.Layout;
import java.util.List;
import java.util.function.Consumer;
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
    
    
    /**
     * Routes to a Party entry form pre-populated with the party specified by an id.
     * @param id The id of the party to use when pre-populating the form.
     * @return 
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ModelAndView getParty(@PathVariable long id) {
        PartyForm party = partyService.getPartyById(id);
        return getPartyModelAndView(party);
    }

    
    /**
     * Route to a list of urls of Parties with names matching the
     * search template. In particular, a case-insensitive search of the Party name.
     * @param partyForm template upon which the search is based
     * @param bindingResult Form validation object to which error messages can be attached
     * @return a route to a page based on a selection list of Parties (urls)
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="findAction")
    public ModelAndView findParty(@ModelAttribute PartyForm partyForm, BindingResult bindingResult) {
        
        List<PartyForm> parties = partyService.findParties(partyForm);
        
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
    
    /*
     * Save or Update a Party, routing back to the Party entry form with a pre-poplulated party.
    */
    private ModelAndView persist(PartyForm partyForm, Errors errors, Consumer<PartyForm> action, String msg) {
        
        if (errors.hasErrors()) {
            return new ModelAndView(ViewRoute.PARTY);
        }
        
        action.accept(partyForm);
        
        PartyForm retrievedPartyForm = partyService.getPartyByName(partyForm.getPartyName());
        
        return getPartyModelAndView(retrievedPartyForm)
                .addObject("notification", retrievedPartyForm.getPartyName() + " has been " + msg + ".");
        
    }
    
    
    /**
     * Persist a new party to a backing data store.
     * @param partyForm the party to be persisted
     * @param errors form validation errors
     * @return a route back to a populated Party form
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="saveAction")
    public ModelAndView saveParty(@Valid @ModelAttribute PartyForm partyForm, Errors errors) {
        Consumer<PartyForm> action = (PartyForm x) -> partyService.saveParty(x);
        return persist(partyForm, errors, action, "saved");
    }

    
    /**
     * Persist an existing party to a backing data store.
     * @param partyForm the party to be persisted
     * @param errors form validation errors
     * @return a route back to a populated Party form
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="updateAction")
    public ModelAndView updateParty(@Valid @ModelAttribute PartyForm partyForm, Errors errors) {
        Consumer<PartyForm> action = (PartyForm x) -> partyService.updateParty(x);
        return persist(partyForm, errors, action, "updated");
    }


    /**
     * Removes an exiting Party from the backing data store.
     * @param partyForm
     * @return 
     */
    @Layout(value = "core/layouts/default")
    @RequestMapping(method = RequestMethod.POST, params="deleteAction")
    public ModelAndView deleteParty(@ModelAttribute PartyForm partyForm) {
        partyService.inactivateParty(partyForm);
        return getPartyModelAndView()
                .addObject("notification", partyForm.getPartyName() + " has been inactivated.");
    }

    
    /**
     * Same effect as GETting a new entry form, but handled via a POST.
     * @return 
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
            ,@Valid @ModelAttribute AddressForm addressForm
            ,@RequestParam long modalAddressId
            ,@RequestParam int modalAddressVersion
            ,Errors errors) {
        
        if (errors.hasErrors()) {
            return new ModelAndView(ViewRoute.PARTY);
        }
        
        if (modalAddressId > 0L) {
            // Update mode
            addressForm.getRecordMetaData().setId(modalAddressId);
            addressForm.getRecordMetaData().setVersion(modalAddressVersion);
            addressService.updateAddress(addressForm);
        } else {
            // Insert mode
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

}
