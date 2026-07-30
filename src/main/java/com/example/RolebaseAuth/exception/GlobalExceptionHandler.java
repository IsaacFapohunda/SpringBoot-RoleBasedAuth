package com.example.RolebaseAuth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiExceptions.class)
    public ResponseEntity<Map<String, Object>> handleException(ApiExceptions apiExceptions){
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", apiExceptions.getMessage());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.valueOf(apiExceptions.getStatus()));
    }
}
