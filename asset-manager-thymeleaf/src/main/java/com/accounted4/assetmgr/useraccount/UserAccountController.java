package com.accounted4.assetmgr.useraccount;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Handle actions involving the manipulation of User Accounts.
 */
@Controller
@Secured("ROLE_USER")
class UserAccountController {

    private final UserAccountRepository accountRepository;

    
    @Autowired
    public UserAccountController(UserAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    

    /**
     * Determine account information for authenticate user in session.
     * @param principal
     * @return 
     */
    @RequestMapping(value = "account/current", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public UserAccount accounts(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findByFullyQualifiedAccountName(principal.getName());
    }
    
}
