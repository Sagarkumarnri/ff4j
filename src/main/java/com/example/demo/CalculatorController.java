package com.example.demo;

import com.example.demo.config.SecureFeatureStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    @Autowired
    private SecureFeatureStore secureFeatureStore;

    @PostMapping("/operate")
    public List<Map<String, Object>> operate(@RequestBody CalculatorBulkRequest request) throws Exception {
        List<Map<String, Object>> responses = new ArrayList<>();

        String featureId = getFeatureIdForOperation(request.getOperation());

        for (String canaryId : request.getCanaryIds()) {
            Map<String, Object> response = new HashMap<>();
            response.put("canaryId", canaryId);

            if (secureFeatureStore.hasAccess(canaryId, featureId)) {
                int result = performOperation(request.getNum1(), request.getNum2(), request.getOperation());
                response.put("message", "Operation Successful");
                response.put("result", result);
            } else {
                response.put("message", "No Access");
            }

            responses.add(response);
        }

        return responses;
    }

    private String getFeatureIdForOperation(String operation) {
        switch (operation.toUpperCase()) {
            case "ADD":
                return "feature-add";
            case "SUB":
                return "feature-sub";
            case "MUL":
                return "feature-mul";
            case "DIV":
                return "feature-div";
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }

    private int performOperation(int num1, int num2, String operation) {
        switch (operation.toUpperCase()) {
            case "ADD":
                return num1 + num2;
            case "SUB":
                return num1 - num2;
            case "MUL":
                return num1 * num2;
            case "DIV":
                if (num2 == 0) throw new ArithmeticException("Cannot divide by zero");
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
}
