package com.example.demo.config;


import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FF4JCalcInitializer implements CommandLineRunner {

    @Autowired
    private FF4j ff4j;

    @Override
    public void run(String... args) {
        createFeature("feature-add", true);
        createFeature("feature-sub", true);
        createFeature("feature-mul", false);
        createFeature("feature-div", false);
    }

    private void createFeature(String name, boolean enabled) {
        if (!ff4j.exist(name)) {
            ff4j.createFeature(name, enabled);
        }
    }
}
