package com.r2dbctest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

/**
 * Project : r2dbc-test
 * Created by IntelliJ IDEA
 * Developer : cheyenneshin
 * Date Time : 2022/08/29 6:12 PM
 * Summary :
 **/

public class ApodException extends RuntimeException{
    @Getter
    int status;
    public ApodException(){super();}
    public ApodException(String message, int status) {super(message); this.status = status;}
    public ApodException(Throwable cause) {super(cause);}
    public ApodException(String message, Throwable cause) {super(message, cause);}

}
