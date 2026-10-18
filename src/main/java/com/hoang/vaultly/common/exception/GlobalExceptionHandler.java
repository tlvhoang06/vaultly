package com.hoang.vaultly.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, Object>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("code", errorCode.getCode());
        body.put("message", errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Invalid request data");

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("code", 4000);
        body.put("message", message);

        return ResponseEntity.badRequest().body(body);
    }
}