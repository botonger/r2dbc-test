package com.r2dbctest.apod.repo;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.r2dbctest.apod.mdl.Apod;
import reactor.core.publisher.Mono;

public interface ApodRepository extends ReactiveCrudRepository<Apod, Long> {

    @Modifying
    @Query("UPDATE apod SET title = :title where id = :id")
    Mono<Integer> setNewTitleFor(Long id, String title);
}
