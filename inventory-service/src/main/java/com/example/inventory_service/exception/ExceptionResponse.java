package com.example.inventory_service.exception;


public record ExceptionResponse(String message) {
    public static ExceptionResponse of(String message) {
        return new ExceptionResponse(message);
    }
}
