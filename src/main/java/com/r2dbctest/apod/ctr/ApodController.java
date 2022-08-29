package com.r2dbctest.apod.ctr;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r2dbctest.apod.mdl.Apod;
import com.r2dbctest.apod.svc.ApodService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apod")
public class ApodController {
    private final ApodService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Apod> retrieveApod() {
        return service.retrieveApodThenSave();
    }

    @GetMapping("/{id}")
    public Mono<Apod> getApodBy(@PathVariable("id") Long id) {
        return service.getApodBy(id);
    }

    @PutMapping
    public Mono<Integer> updateApod(@RequestBody Apod apod) {
        return service.update(apod);
    }

    @DeleteMapping("/{id}")
    public Mono<Integer> deleteApod(@PathVariable("id") Long id) {
        return service.deleteApodBy(id);
    }
}
