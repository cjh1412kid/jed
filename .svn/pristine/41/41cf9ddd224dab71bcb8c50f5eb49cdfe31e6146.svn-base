package io.nuite;

import io.nuite.datasources.DynamicDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class })
@Import({DynamicDataSourceConfig.class})
public class GoodsPullApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) {
        SpringApplication.run(GoodsPullApplication.class, args);
    }
    
    
   
    
}
