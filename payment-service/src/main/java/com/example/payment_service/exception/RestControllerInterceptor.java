package com.example.payment_service.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class RestControllerInterceptor {

    @ExceptionHandler(InvalidSignatureException.class)
    public ExceptionResponse handleInvalidSignatureException(InvalidSignatureException exception) {
        return ExceptionResponse.of(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse handleNotFoundException(NotFoundException exception) {
        return ExceptionResponse.of(exception.getMessage());
    }

}
