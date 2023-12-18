package com.example.devops.service;

import com.example.devops.exceptions.InvalidOperationException;

public interface MathOperator {
    double doMath(double operand1, double operand2, String operation) throws InvalidOperationException;
}
