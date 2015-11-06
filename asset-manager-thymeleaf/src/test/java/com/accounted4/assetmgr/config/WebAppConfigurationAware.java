package com.accounted4.assetmgr.config;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * Instruct JUnit to run these tests using the Spring test runner which will provide the
 * web context.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")  // add test scope beans to the context
@WebAppConfiguration     // use a "web" context when loading contexts for tests
@ContextConfiguration(classes = {
        ApplicationConfig.class,
        EmbeddedDataSourceConfig.class,
        DataSourceConfig.class,
        NoCsrfSecurityConfig.class,
        WebMvcConfig.class
})
public abstract class WebAppConfigurationAware {

    @Inject
    protected WebApplicationContext wac;
    protected MockMvc mockMvc;

    @Before
    public void before() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

}
