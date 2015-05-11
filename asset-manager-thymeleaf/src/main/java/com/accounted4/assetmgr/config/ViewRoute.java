package com.accounted4.assetmgr.config;

/**
 * Centralization of names used to refer to the pages (views) presented
 * to the end user.  Each request results in a response which is the
 * name of the page to present on the ui.
 * 
 * In one way, the view routes and handlers should be exposed as
 * a routing service: then each module can register it's own routes.
 * On the other hand, if we want to use the paths as annotation
 * parameters (i.e. RequestMapping), we need the paths to be constants.
 * This would cause problems with parameterized urls.
 */
public interface ViewRoute {

    static final String HOME_SIGNED_IN     = "/core/auth/homeSignedIn";
    static final String HOME_NOT_SIGNED_IN = "/core/auth/homeNotSignedIn";
    static final String SIGN_IN            = "/core/auth/signin";
    static final String SIGN_UP            = "/core/auth/signup";
    
    static final String SELECT_LIST        = "/fragments/selectList";
    
    static final String PARTY              = "/core/party/party";
    
}
