package com.test.springboot_html;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EquationController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @PostMapping("/equation")
    public ResponseEntity<String> solveEquation(@RequestBody Map<String, String> request) {
        String equation = request.get("equation");
        Map<String, Object> response = new HashMap<>();
        System.out.println("Otrzymano równanie: " + equation);
        try {
            Expression expression = new ExpressionBuilder(equation).build();
            double result = expression.evaluate();

            response.put("equation", equation);
            response.put("result", result);
            return ResponseEntity.ok(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            response.put("error", "Błędne równanie");
            try {
                return ResponseEntity.badRequest()
                        .body(objectMapper.writeValueAsString(response));
            } catch (Exception jsonError) {
                return ResponseEntity.badRequest().body("{\"error\": \"Błąd parsowania\"}");
            }
        }
    }
}
