package com.example.demo.config;

import com.example.demo.config.SecureFeatureStore;
import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.ff4j.store.JdbcFeatureStore;
import org.ff4j.property.store.JdbcPropertyStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Configuration
public class FF4jConfig {

    @Bean
    public FF4j ff4j(DataSource dataSource) {
        FF4j ff4j = new FF4j();

        JdbcFeatureStore jdbcStore = new JdbcFeatureStore(dataSource);
        jdbcStore.createSchema();

        // Wrap with permission-checking logic
        SecureFeatureStore secureStore = new SecureFeatureStore(jdbcStore);



        ff4j.setFeatureStore(secureStore);
        ff4j.setPropertiesStore(new JdbcPropertyStore(dataSource));
        ff4j.audit(false);

        return ff4j;
    }
}
