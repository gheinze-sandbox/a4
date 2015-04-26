package com.accounted4.assetmgr.auth;

import com.accounted4.assetmgr.config.ViewRoute;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for the default entry point of the application. The
 * view returned to the requester depends whether the session has
 * been created with an authenticated user or not.
 * 
 */
@Controller
public class HomeController {

    /**
     *  Handle routing for the home page view.
     * 
     * @param principal containing the currently authenticated user, implicitly available
     * as an @RequestMapping handler parameter (see Spring Reference: @RequestMapping Handler Methods.
     * 
     * @return the view reference which will be dependent on whether an authenticated user is logged
     * in or not.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String handleHomePageRequest(Principal principal) {
        return principal != null ? ViewRoute.HOME_SIGNED_IN : ViewRoute.HOME_NOT_SIGNED_IN;
    }
    
}
