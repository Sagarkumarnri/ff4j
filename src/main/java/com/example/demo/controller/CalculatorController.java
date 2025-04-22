package com.example.demo.controller;

import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calc")
public class CalculatorController {

    @Autowired
    private FF4j ff4j;

    @GetMapping("/operate")
    public String operate(
            @RequestParam int a,
            @RequestParam int b,
            @RequestParam String op // accepted: add, sub, mul, div
    ) {
        switch (op.toLowerCase()) {
            case "add":
                if (ff4j.check("feature-add")) return "Result: " + (a + b);
                return "Addition is disabled via feature toggle.";

            case "sub":
                if (ff4j.check("feature-sub")) return "Result: " + (a - b);
                return "Subtraction is disabled via feature toggle.";

            case "mul":
                if (ff4j.check("feature-mul")) return "Result: " + (a * b);
                return "Multiplication is disabled via feature toggle.";

            case "div":
                if (!ff4j.check("feature-div")) return "Division is disabled via feature toggle.";
                if (b == 0) return "Cannot divide by zero.";
                return "Result: " + (a / b);

            default:
                return "Unsupported operation. Use: add, sub, mul, div";
        }
    }
}
