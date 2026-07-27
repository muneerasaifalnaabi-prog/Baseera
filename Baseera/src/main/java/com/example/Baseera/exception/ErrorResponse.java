package com.example.Baseera.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Date timestamp;
    private int statusCode;
    private HttpStatus status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> fieldErrors; // only populated on validation errors, null otherwise

    // Standard errors — no field-level detail
    public ErrorResponse(HttpStatus status, int statusCode, String error, String message, String path) {
        this.timestamp = new Date();
        this.status = status;
        this.statusCode = statusCode;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Validation errors — carries the field-level error map
    public ErrorResponse(HttpStatus status, int statusCode, String error, String message, String path, Map<String, String> fieldErrors) {
        this(status, statusCode, error, message, path);
        this.fieldErrors = fieldErrors;
    }
}