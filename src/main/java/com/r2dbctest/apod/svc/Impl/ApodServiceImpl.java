package com.r2dbctest.apod.svc.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.query.Criteria.CriteriaStep;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.r2dbctest.apod.mdl.Apod;
import com.r2dbctest.apod.repo.ApodRepository;
import com.r2dbctest.apod.svc.ApodService;
import com.r2dbctest.exception.GlobalExceptionHandler;
import com.r2dbctest.exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.r2dbctest.exception.GlobalExceptionHandler.TitleNotProperException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ApodServiceImpl implements ApodService {
    @NonNull
    private WebClient webClient;
    private ApodRepository repository;
    private R2dbcEntityOperations template;

    public ApodServiceImpl(WebClient webClient, ApodRepository repository, R2dbcEntityOperations template) {
        this.webClient = webClient;
        this.repository = repository;
        this.template = template;
    }

    @Override
    public Flux<Apod> retrieveApodThenSave() {
        ////////////template
        return webClient.get().retrieve().bodyToMono(Apod.class)
                         .flatMap(template::insert)
                .thenMany(template.select(Apod.class).all())
                .take(1)
                ;

        ////////////repository
        /*
        return webClient.get().retrieve().bodyToMono(Apod.class)
                 .flatMap(repository::save)
                 .thenMany(repository.findAll());
        */
}

    @Override
    public Mono<Apod> getApodBy(Long id) {
        ////////////template
//        return template.selectOne(Query.query(Criteria.where("id").is(id)), Apod.class);

        ////////////template - more complicated example
        /*
        return template.select(Apod.class)
                .from("apod")
                .matching(Query.query(Criteria.where("title").is("test-title")
                                              .and("id").in(1,2))
                                  .sort(Sort.by(Order.desc("id"))))
                .one();
         */

        ////////////repository
        ///*
        return repository.findById(id)
                         .switchIfEmpty(Mono.error(new ResourceNotFoundException()));

//@Transactional
//                .switchIfEmpty(repository.save(new Apod("title", "copyright", null, null, null, null, null, null)))
//                .then(repository.deleteAll())
//                .then(repository.save(new Apod("title2", "copyright", null, null, null, null, null, null)))
//                ;
         //*/
    }

    @Override
    public Mono<Integer> update(Apod apod) {
        ////////////template
        return template.update(Apod.class)
                .inTable("apod")
                .matching(Query.query(Criteria.where("id").is(apod.getId())))
                .apply(Update.update("title", apod.getTitle()));

        ////////////repository - modifying queries
        /*
        return repository.setNewTitleFor(apod.getId(), apod.getTitle());
        */

        ////////////repository: response type -> Mono<Apod>
        /* setTitle() 사용하는 로직에서만 null 체크
        return repository.findById(apod.getId())
                .flatMap(updated -> {
                    updated.setTitle(apod.getTitle());
                    return repository.save(updated);
                });
         */
    }

    @Override
    public Mono<Integer> deleteApodBy(Long id) {
        ////////////template
        return template.delete(Apod.class)
                .from("apod")
                .matching(Query.query(Criteria.where("id").is(id)))
                .all();

        ////////////repository: response type -> Mono<Void>
        /*
        return repository.findById(id)
                         .switchIfEmpty(Mono.error(new Throwable("not found")))
                         .flatMap(repository::delete);
        */
    }
}
