package com.example.devops.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class DoMathRequest {
    private double operand1;
    private double operand2;
    private String operation;

}