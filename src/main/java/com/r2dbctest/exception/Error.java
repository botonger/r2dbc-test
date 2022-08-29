package com.r2dbctest.exception;

import lombok.Getter;

@Getter
public enum Error {
    TITLE_NOT_PROPER(400, "TITLE NOT PROPER"),
    RESOURCE_NOT_FOUND(404, "RESOURCE NOT FOUND");
    int statusCode; String message;

    Error(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
