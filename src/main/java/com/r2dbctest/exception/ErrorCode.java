package com.r2dbctest.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    TITLE_NOT_PROPER(100, "TITLE NOT PROPER"),
    RESOURCE_NOT_FOUND(101, "RESOURCE NOT FOUND");
    int statusCode; String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
