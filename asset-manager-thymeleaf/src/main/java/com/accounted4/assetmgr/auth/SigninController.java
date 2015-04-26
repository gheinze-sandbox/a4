package com.accounted4.assetmgr.auth;

import com.accounted4.assetmgr.config.ViewLayout;
import com.accounted4.assetmgr.config.ViewRoute;
import com.accounted4.assetmgr.support.web.Layout;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handle request for the presentation of a sign in form.
 * 
 * Note: the handler for the "signin" post is handled by
 * SpringSecurity as defined in the .formLogin().loginPage("/signin")
 * configuration of SpringSecurity.
 */
@Controller
public class SigninController {

    /**
     * Respond to a "signin" GET request by presenting the login form.
     * @return 
     */
    @Layout(value = ViewLayout.VANILLA)
    @RequestMapping(value = "signin")
    public String signin() {
        return ViewRoute.SIGN_IN;
    }
    
}
