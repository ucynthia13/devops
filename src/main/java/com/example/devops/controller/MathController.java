package com.example.devops.controller;

import com.example.devops.dto.DoMathRequest;
import com.example.devops.exceptions.InvalidOperationException;
import com.example.devops.service.MathOperator;
import com.example.devops.serviceImpl.MathOperatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/math")
public class MathController {

    private final MathOperator mathOperator;

    @Autowired
    public MathController(MathOperator mathOperator) {
        this.mathOperator = mathOperator;
    }

    @PostMapping("/doMath")
    public ResponseEntity<?> doMath(@RequestBody DoMathRequest doMathRequest) {
        try {
            double result = mathOperator.doMath(
                    doMathRequest.getOperand1(),
                    doMathRequest.getOperand2(),
                    doMathRequest.getOperation()
            );

            Map<String, Double> response = Collections.singletonMap("calcResponse", result);
            return ResponseEntity.ok(response);

        } catch (InvalidOperationException e) {
            // Handle invalid operation exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Internal Server Error"));
        }
    }
}