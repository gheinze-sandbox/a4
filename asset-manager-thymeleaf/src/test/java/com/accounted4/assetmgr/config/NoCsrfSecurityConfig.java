package com.accounted4.assetmgr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * For testing purposes, we will trust the authenticated client and not force token passing.
 */
@Configuration
@Order(1)  // Apply ordering to avoid duplicate instantiation problems between test and inherited prod
public class NoCsrfSecurityConfig extends SecurityConfig {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
    }

}
