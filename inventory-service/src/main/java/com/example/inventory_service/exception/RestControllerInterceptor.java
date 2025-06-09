package com.example.inventory_service.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerInterceptor {

    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse handleNotFoundException(NotFoundException exception) {
        return ExceptionResponse.of(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ExceptionResponse handleBadRequestException(BadRequestException exception) {
        return ExceptionResponse.of(exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ExceptionResponse handleIllegalArgumentException(ForbiddenException exception) {
        return ExceptionResponse.of(exception.getMessage());
    }

}
