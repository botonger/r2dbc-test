package com.r2dbctest.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //exeption, handler 분리

    @ExceptionHandler(ApodException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    protected ErrorResponse handleApodException(ServerHttpRequest request, ApodException ex) {
        log.error("error: " + ex.getMessage());
        return ErrorResponse.of(ex.getMessage(), ex.getStatus(), request);
    }

    @Getter
    private static class ErrorResponse {
        final String timestamp, path, error;
        final int status;

        private ErrorResponse(String path, String error, int status){
            timestamp = LocalDateTime.now().toString();
            this.path = path;
            this.error = error;
            this.status = status;
        }
        static ErrorResponse of(String error, int status, ServerHttpRequest request) {
            return new ErrorResponse(request.getURI().getPath(), error, status);
        };
    }
}
