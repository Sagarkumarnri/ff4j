package com.example.demo.controller;

import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeatureController {

    @Autowired
    private FF4j ff4j;

    @GetMapping("/check-feature")
    public String checkFeature() {
        return ff4j.check("my-feature") ? "Feature is enabled!" : "Feature is disabled!";
    }
}
