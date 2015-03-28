package com.accounted4.assetmgr.auth;

import com.accounted4.assetmgr.support.web.MessageHelper;
import com.accounted4.assetmgr.useraccount.UserService;
import com.accounted4.assetmgr.useraccount.UserAccount;
import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.support.web.Layout;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Handle request for the presentation of a sign up form used
 * to create a public account.
 */
@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    
    /**
     * Present the "signup" form for creating a public account.
     * 
     * @param model A backing been for the form named "signupForm" is added
     * as a Spring model attribute which places it into the ThymeLeaf context.
     * 
     * @return name of the view fragment used to show the signup form.
     */
    @Layout(value = "core/layouts/vanilla")
    @RequestMapping(value = "signup")
    public String signup(Model model) {
        model.addAttribute(new SignupForm());
        return ViewRoute.SIGN_UP;
    }

    
    /**
     * The handler for the submission of a signup form to create a Public account.
     * 
     * @param signupForm
     * @param errors
     * @param ra
     * @return 
     */
    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
        
        if (errors.hasErrors()) {
            return ViewRoute.SIGN_UP;
        }
        
        UserAccount account = userService.createPublicUserAccount(signupForm.getAccountName(), signupForm.getPassword());
        userService.signin(account);
        // see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
        MessageHelper.addSuccessAttribute(ra, "signup.success");
        return "redirect:/";
        
    }
    
}
