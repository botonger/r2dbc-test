package com.r2dbctest.apod.svc.Impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import com.r2dbctest.apod.svc.ApodService;

import reactor.test.StepVerifier;

//@SpringBootTest
@WebFluxTest({ApodServiceImplTest.class})
class ApodServiceImplTest {
    @MockBean
    ApodService apodService;

    @Test
    void retrieveApodTest(@Autowired WebClient client) {

    }

}
