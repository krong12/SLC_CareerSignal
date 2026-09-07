package com.slc.mentoring.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(
            CustomException exception
    ) {
        ExceptionCode code = exception.getExceptionCode();

        return ResponseEntity
                .status(code.getStatusCode())
                .body(Map.of(
                        "code", code.name(),
                        "message", code.getMessage()
                ));
    }
}