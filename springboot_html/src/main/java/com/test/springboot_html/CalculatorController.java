package com.test.springboot_html;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "*")
public class CalculatorController {
    private static final Logger LOGGER = Logger.getLogger(CalculatorController.class.getName());

    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculate(@RequestBody Map<String, Object> payload) {
        LOGGER.info("Received calculation request: " + payload);

        Map<String, Object> response = new HashMap<>();

        try {
            String expression = (String) payload.get("expression");
            Number result = (Number) payload.get("result");

            response.put("originalExpression", expression);
            response.put("result", result);
            response.put("status", "success");

            LOGGER.info("Calculation: " + expression + " = " + result);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            LOGGER.severe("Calculation error: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }
}
