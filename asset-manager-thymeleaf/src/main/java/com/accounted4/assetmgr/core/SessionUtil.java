package com.accounted4.assetmgr.core;

import com.accounted4.assetmgr.useraccount.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Static utilities for retrieving Session related information.
 * 
 */
public class SessionUtil {

    /**
     * org id of the user in session.
     * TODO: what if there is no authenticated user?  Guest?
     * @return 
     */
    public static int getSessionOrigId() {
        AppUserDetails user = (AppUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getOrgId();
    }
        
}
