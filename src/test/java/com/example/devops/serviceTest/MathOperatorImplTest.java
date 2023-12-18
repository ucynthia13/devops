package com.example.devops.serviceTest;

import com.example.devops.exceptions.InvalidOperationException;
import com.example.devops.service.MathOperator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ComponentScan(basePackages = "com.example.devops")

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