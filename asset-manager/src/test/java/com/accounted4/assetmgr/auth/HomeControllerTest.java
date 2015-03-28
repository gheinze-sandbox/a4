package com.accounted4.assetmgr.auth;

import com.accounted4.assetmgr.config.WebAppConfigurationAware;
import java.security.Principal;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import org.junit.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Verify that the HomeController renders a different page depending on whether
 * there is an authenticated user in context or not.
 * 
 * Because of the inversion introduced by templating,
 * view().getName(ViewRoute.HOME_SIGNED_IN)
 * will fail: the view name is the template layout whereas our route is the content fragment.
 */
public class HomeControllerTest extends WebAppConfigurationAware {

    /**
     * If a user (Principal) is in context, the home page route
     * should display the "signed in" home page.
     * 
     * @throws Exception 
     */
    @Test
    public void displaysHomeSignedIn() throws Exception {
        
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "HomeControllerUnitTest";
            }
        };
       
        mockMvc.perform(get("/").secure(true).principal(principal))
                .andExpect(content().string(
                        allOf(
                                containsString("<div id=\"homeSignedIn\"")
                        )))
                ;
        
    }

    
    @Test
    public void displaysHomeNotSignedIn() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(content().string(
                        allOf(
                                containsString("<div id=\"homeNotSignedIn\"")
                        )))
                ;
    }

    
}
