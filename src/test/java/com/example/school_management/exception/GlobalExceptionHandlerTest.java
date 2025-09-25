package com.example.school_management.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleRuntimeException_NotFound() {
        RuntimeException exception = new RuntimeException("Student not found");
        
        ResponseEntity<String> response = globalExceptionHandler.handleRuntimeException(exception);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Student not found", response.getBody());
    }

    @Test
    void testHandleRuntimeException_Conflict() {
        RuntimeException exception = new RuntimeException("CPF already exists");
        
        ResponseEntity<String> response = globalExceptionHandler.handleRuntimeException(exception);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CPF already exists", response.getBody());
    }

    @Test
    void testHandleRuntimeException_BadRequest() {
        RuntimeException exception = new RuntimeException("Invalid CPF format");
        
        ResponseEntity<String> response = globalExceptionHandler.handleRuntimeException(exception);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid CPF format", response.getBody());
    }

    @Test
    void testHandleRuntimeException_Default() {
        RuntimeException exception = new RuntimeException("Some other error");
        
        ResponseEntity<String> response = globalExceptionHandler.handleRuntimeException(exception);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody());
    }
}
