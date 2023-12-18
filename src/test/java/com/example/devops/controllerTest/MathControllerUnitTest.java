package com.example.devops.controllerTest;

import com.example.devops.controller.MathController;
import com.example.devops.dto.DoMathRequest;
import com.example.devops.exceptions.InvalidOperationException;
import com.example.devops.serviceImpl.MathOperatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MathControllerUnitTest {

    @Mock
    private MathOperatorImpl mathOperator;

    @InjectMocks
    private MathController mathController;

    @Test
    public void testDoMath_Success() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 4, "*");

        when(mathOperator.doMath(5, 4, "*")).thenReturn(20.0);

        // Act
        ResponseEntity<?> responseEntity = mathController.doMath(request);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void testDoMath_DivideByZero() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 0, "/");
        when(mathOperator.doMath(5, 0, "/")).thenThrow(new InvalidOperationException("Cannot divide by 0"));

        // Act
        ResponseEntity<?> responseEntity = mathController.doMath(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertTrue(responseEntity.getBody().containsKey("error"));
    }

    @Test
    public void testDoMath_UnknownOperation() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 4, "%");
        when(mathOperator.doMath(5, 4, "%")).thenThrow(new RuntimeException("Unknown operation"));

        // Act
        ResponseEntity<?> responseEntity = mathController.doMath(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
//        assertTrue(responseEntity.getBody().containsKey("error"));
    }
}
