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
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.client.WebClient;

import com.r2dbctest.apod.mdl.Apod;
import com.r2dbctest.apod.repo.ApodRepository;
import com.r2dbctest.apod.svc.ApodService;
import com.r2dbctest.exception.ApodException;
import com.r2dbctest.exception.GlobalExceptionHandler;


import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ApodServiceImpl implements ApodService {
    @NonNull
    private final WebClient webClient;
    private final ApodRepository repository;
    private final R2dbcEntityOperations template;
    private final TransactionalOperator transactionalOperator;

    public ApodServiceImpl(WebClient webClient,
                           ApodRepository repository,
                           @Qualifier("postgresR2dbcEntityOperations") R2dbcEntityOperations template,
                           @Qualifier("postgresOperator") TransactionalOperator transactionalOperator
    ) {
        this.webClient = webClient;
        this.repository = repository;
        this.template = template;
        this.transactionalOperator = transactionalOperator;
    }

//    @Transactional( rollbackFor = {Exception.class, Throwable.class})
//    @Transactional("postgresTransactionManager") //reactiveTransanctionalManager 명시해줘야 transactional 작동 -> TransactionManager는 하나 이상일 수 있다.
    //-> mariadb에서는 작동하는데 Postgresql에서는 안됨
//    @Transactional
    @Override
    public Flux<Apod> retrieveApodThenSave() {
        ////////////template
        return webClient.get().retrieve().bodyToMono(Apod.class)
                        .flatMap(template::insert)
                        .then(Mono.error(new Throwable("test")))
                        .thenMany(template.select(Apod.class).all())
                        .takeUntil(v->v.getId()==3)
                        .as(transactionalOperator::transactional)
//                        .take(1)
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
                         .switchIfEmpty(Mono.error(new ApodException()));

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
