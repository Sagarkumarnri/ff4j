package com.example.demo.config;

import org.ff4j.FF4j;
import org.ff4j.store.JdbcFeatureStore;
import org.ff4j.property.store.JdbcPropertyStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FF4jConfig {

    @Bean
    public FF4j ff4j(DataSource dataSource) {
        FF4j ff4j = new FF4j();

        JdbcFeatureStore featureStore = new JdbcFeatureStore(dataSource);
        featureStore.createSchema();

        JdbcPropertyStore propertyStore = new JdbcPropertyStore(dataSource);

        ff4j.setFeatureStore(featureStore);
        ff4j.setPropertiesStore(propertyStore);
        ff4j.audit(false);
        return ff4j;
    }
}
