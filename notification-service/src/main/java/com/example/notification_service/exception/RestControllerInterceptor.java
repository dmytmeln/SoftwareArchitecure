package com.example.notification_service.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class RestControllerInterceptor {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleIllegalStateException(IllegalStateException e) {
        log.error("Error during sending email occurred: {}", e.getMessage(), e);
        return ExceptionResponse.of(e.getMessage());
    }

}
