package com.accounted4.assetmgr.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.accounted4.assetmgr.useraccount.UserService;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Authentication and Authorization support via SpringSecurity. Initiates a filter chain
 * to intercept incoming request and perform authentication checks before allowing the
 * request to be forwarded on to the DispatcherServlet.
 * 
 */
@Configuration
@Order(2)   // Apply ordering to avoid duplicate instantiation problems between test and prod
@EnableWebMvcSecurity
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REMEMBER_ME_KEY = "remember-me-key";
    
    
    @Bean
    public UserService userService() {
        return new UserService();
    }

    
    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        return new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userService());
    }

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
	}

    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .eraseCredentials(true)
            .userDetailsService(userService())
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
            .authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/resources/**", "/signup").permitAll()
                .anyRequest().authenticated()
                .and()
                
            .formLogin()
                .loginPage("/signin")
                .permitAll()
                .failureUrl("/signin?error=1")
                .loginProcessingUrl("/authenticate")
                .and()
                
            .logout()
                .logoutUrl("/logout")
                .permitAll()
                .logoutSuccessUrl("/signin?logout")
                .and()
                
            .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(REMEMBER_ME_KEY)
            ;
        
    }
    
}