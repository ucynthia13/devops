package com.example.devops.controllerTest;

import com.example.devops.controller.MathController;
import com.example.devops.dto.DoMathRequest;
import com.example.devops.exceptions.InvalidOperationException;
import com.example.devops.service.MathOperator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MathController.class)
public class MathControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MathOperator mathOperator;

    @Test
    public void testDoMath_Success() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 4, "*");
        when(mathOperator.doMath(5, 4, "*")).thenReturn(20.0);

        // Act
        MvcResult result = mockMvc.perform(post("/api/math/doMath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("\"calcResponse\":20.0"));
    }

    @Test
    public void testDoMath_DivideByZero() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 0, "/");
        when(mathOperator.doMath(5, 0, "/")).thenThrow(new InvalidOperationException("Cannot divide by 0"));

        // Act
        MvcResult result = mockMvc.perform(post("/api/math/doMath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("\"error\":\"Cannot divide by 0\""));
    }

    @Test
    public void testDoMath_UnknownOperation() throws Exception {
        // Arrange
        DoMathRequest request = new DoMathRequest(5, 4, "%");
        when(mathOperator.doMath(5, 4, "%")).thenThrow(new RuntimeException("Unknown operation"));

        // Act
        MvcResult result = mockMvc.perform(post("/api/math/doMath")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isInternalServerError())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("\"error\":\"Internal Server Error\""));
    }

    // Helper method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
