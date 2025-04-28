package com.example.demo.config;


import org.ff4j.store.InMemoryFeatureStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecureFeatureStoreConfig {

    @Bean
    public SecureFeatureStore secureFeatureStore() {
        InMemoryFeatureStore delegate = new InMemoryFeatureStore(); // Or JDBC-backed FeatureStore later
        return new SecureFeatureStore(delegate);
    }
}
