package com.example.devops.service;

import com.example.devops.exceptions.InvalidOperationException;
import org.springframework.stereotype.Repository;

@Repository
public interface MathOperator {
    double doMath(double operand1, double operand2, String operation) throws InvalidOperationException;
}
