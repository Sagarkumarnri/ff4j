package com.example.demo.controller;


import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calc")
public class CalculatorController {

    @Autowired
    private FF4j ff4j;

    @GetMapping("/add")
    public String add(@RequestParam int a, @RequestParam int b) {
        if (ff4j.check("feature-add")) {
            return "Result: " + (a + b);
        }
        return "Addition is disabled via feature toggle.";
    }

    @GetMapping("/sub")
    public String subtract(@RequestParam int a, @RequestParam int b) {
        if (ff4j.check("feature-sub")) {
            return "Result: " + (a - b);
        }
        return "Subtraction is disabled via feature toggle.";
    }

    @GetMapping("/mul")
    public String multiply(@RequestParam int a, @RequestParam int b) {
        if (ff4j.check("feature-mul")) {
            return "Result: " + (a * b);
        }
        return "Multiplication is disabled via feature toggle.";
    }

    @GetMapping("/div")
    public String divide(@RequestParam int a, @RequestParam int b) {
        if (ff4j.check("feature-div")) {
            if (b == 0) return "Cannot divide by zero.";
            return "Result: " + (a / b);
        }
        return "Division is disabled via feature toggle.";
    }
}
