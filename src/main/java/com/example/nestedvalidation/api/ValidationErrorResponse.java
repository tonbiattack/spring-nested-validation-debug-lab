package com.example.nestedvalidation.api;

import java.util.List;

public record ValidationErrorResponse(String code, List<FieldViolation> errors) {

    public record FieldViolation(String field, String message) {
    }
}
