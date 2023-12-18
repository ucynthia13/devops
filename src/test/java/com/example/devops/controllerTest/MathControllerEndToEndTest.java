package com.example.devops.controllerTest;

import com.example.devops.dto.DoMathRequest;
import com.example.devops.exceptions.InvalidOperationException;
import com.example.devops.service.MathOperator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// End-to-End Test
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MathControllerEndToEndTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private MathOperator mathOperator;

    @Test
    public void testDoMath_Success() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 4, "*");
        when(mathOperator.doMath(5, 4, "*")).thenReturn(20.0);

        // Act
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/api/math/doMath", request, String.class);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("\"calcResponse\":20.0"));
    }

    @Test
    public void testDoMath_DivideByZero() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 0, "/");
        when(mathOperator.doMath(5, 0, "/")).thenThrow(new InvalidOperationException("Cannot divide by 0"));

        // Act
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/api/math/doMath", request, String.class);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("\"error\":\"Cannot divide by 0\""));
    }

    @Test
    public void testDoMath_UnknownOperation() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 4, "%");
        when(mathOperator.doMath(5, 4, "%")).thenThrow(new RuntimeException("Unknown operation"));

        // Act
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("/api/math/doMath", request, String.class);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("\"error\":\"Internal Server Error\""));
    }
}
