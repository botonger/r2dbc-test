package com.r2dbctest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@SpringBootApplication
public class R2dbcTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcTestApplication.class, args);
    }

}
