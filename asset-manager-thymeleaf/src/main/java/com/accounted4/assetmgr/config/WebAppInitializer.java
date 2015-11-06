package com.accounted4.assetmgr.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

/**
 * Register and configure the DispatcherServlet which performs the routing
 * of the incoming request to the appropriate @Controller handler.
 * Creates both a DispatcherServlet and a ContextLoadListener.
 *
 * The DispatcherServlet loads the application context with beans defined in the WebConfig config class
 * (beans containing web components such as controllers, view resolvers, handler mappings)
 * These are defined in WebMvcConfig.java
 *
 * The ContextLoadListener is expected to load other beans such as middle tier and data tier components.
 * These are defined in ApplicationConfig.java
 *
 * Servlet 3.0 container will scan classpath for implementations of javax.servlet.ServletContainerInitializer
 * and use that for  servlet configuation. This is a Spring implementation of the Initializer.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {ApplicationConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {WebMvcConfig.class};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        DelegatingFilterProxy securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");

        return new Filter[] {characterEncodingFilter, securityFilterChain};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("defaultHtmlEscape", "true");
        registration.setInitParameter("spring.profiles.active", "default");
    }
}