package com.accounted4.assetmgr.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;


/**
 * Add a DataSource and TransactionManager into the Spring context.
 * @author gheinze
 */
@Configuration
@EnableTransactionManagement
class DataSourceConfig implements TransactionManagementConfigurer {

    // Data source config
    @Value("${dataSource.driverClassName}")
    private String driver;
    
    @Value("${dataSource.url}")
    private String url;
    
    @Value("${dataSource.username}")
    private String username;
    
    @Value("${dataSource.password}")
    private String password;
    
    
    
    @Bean
    public DataSource dataSource() {
        
        HikariConfig config = new HikariConfig();
        
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(config);
    }
    
    
    @Bean
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
}
