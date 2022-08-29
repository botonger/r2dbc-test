package com.r2dbctest.apod.svc;


import com.r2dbctest.apod.mdl.Apod;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ApodService {
    Flux<Apod> retrieveApodThenSave();
    Mono<Apod> getApodBy(Long id);
    Mono<Integer> update(Apod apod);
    Mono<Integer> deleteApodBy(Long id);
}
