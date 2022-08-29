package com.r2dbctest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    public static class TitleNotProperException extends CustomException{ public TitleNotProperException(){}}
    public static class ResourceNotFoundException extends CustomException { public ResourceNotFoundException() {}}
    public static class CustomException extends RuntimeException {
        Error error;
        public CustomException() {
            super(Error.RESOURCE_NOT_FOUND.getMessage());
            error = Error.RESOURCE_NOT_FOUND;
        }
    }

    @ExceptionHandler({TitleNotProperException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTitleNotProper(ServerWebExchange exchange, TitleNotProperException ex) {
        log.error("error: " + ex.getMessage());
        return new ErrorResponse(exchange, ex.error);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ErrorResponse handleTitleNotProper(ServerWebExchange exchange, ResourceNotFoundException ex) {
        log.error("error: " + ex.getMessage());
        return new ErrorResponse(exchange, ex.error);
    }
}
