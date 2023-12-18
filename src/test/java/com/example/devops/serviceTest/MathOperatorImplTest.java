package com.example.devops.serviceTest;

import com.example.devops.exceptions.InvalidOperationException;
import com.example.devops.service.MathOperator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MathOperatorImplTest {

    @Autowired
    private MathOperator mathOperator;

    @Test
    public void testDoMath() throws InvalidOperationException {

        // performing unit tests for each operation
        assertEquals(9, mathOperator.doMath(5, 4, "+"));
        assertEquals(1.25, mathOperator.doMath(5, 4, "/"));
        assertEquals(20, mathOperator.doMath(5, 4, "*"));
        assertEquals(1, mathOperator.doMath(5, 4, "-"));

        // Test division by zero
        assertThrows(InvalidOperationException.class, () -> mathOperator.doMath(5, 0, "/"));
    }
}