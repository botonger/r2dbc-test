package com.r2dbctest.exception;

import java.time.LocalDateTime;

import org.springframework.web.server.ServerWebExchange;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponse {
    private final String timestamp = LocalDateTime.now().toString();
    private String path, error;
    private int status;

    public ErrorResponse(ServerWebExchange exchange, Error errorCode) {
        path = exchange.getRequest().getPath().toString();
        error = errorCode.getMessage();
        status = errorCode.getStatusCode();
    }
}
