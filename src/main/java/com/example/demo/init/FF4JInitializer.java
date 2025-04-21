package com.example.demo.init;

import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FF4JInitializer implements CommandLineRunner {

    @Autowired
    private FF4j ff4j;

    @Override
    public void run(String... args) {
        if (!ff4j.exist("my-feature")) {
            ff4j.createFeature("my-feature", true);
        }
    }
}
