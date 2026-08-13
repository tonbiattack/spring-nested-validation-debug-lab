package com.example.nestedvalidation.api;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handle(MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse.FieldViolation> errors = exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::toFieldViolation)
            .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ValidationErrorResponse("VALIDATION_ERROR", errors));
    }

    private ValidationErrorResponse.FieldViolation toFieldViolation(FieldError fieldError) {
        return new ValidationErrorResponse.FieldViolation(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
