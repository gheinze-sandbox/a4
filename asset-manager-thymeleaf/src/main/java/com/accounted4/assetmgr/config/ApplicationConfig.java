package com.accounted4.assetmgr.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.accounted4.assetmgr.Application;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Generic Spring configuration.
 */
@Configuration
@ComponentScan(basePackageClasses = Application.class)
class ApplicationConfig {
            

    /**
     * Load contents of property files into the container.
     * @return 
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("/persistence.properties"));
        return ppc;
    }
	
}