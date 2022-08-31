package com.r2dbctest.apod.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.r2dbctest.apod.mdl.Apod;
import com.r2dbctest.config.PostgresConfiguration;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Project : r2dbc-test
 * Created by IntelliJ IDEA
 * Developer : cheyenneshin
 * Date Time : 2022/08/30 10:46 PM
 * Summary :
 **/
@DataR2dbcTest
@Import({ PostgresConfiguration.class })
@Slf4j
class ApodRepositoryTest {
    @Autowired
    ApodRepository apodRepository;
    @Autowired
    @Qualifier("postgresR2dbcEntityOperations")
    R2dbcEntityOperations template;
    @Autowired
    @Qualifier("postgresql")
    ConnectionFactory factory;
    private Apod apod1;


    @BeforeEach
    void before() {
        Hooks.onOperatorDebug();
        log.info("=== start repo test ===");
//        apodRepository.deleteAll().subscribe();
        apod1 = new Apod("title", "copyright", "2022-08-31",
                              "explanation", "hdurl", "mediaType",
                              "url", null);
    }
    @Test
    void create() {
        apodRepository.save(apod1)
                .thenMany(apodRepository.findAll())
                .as(StepVerifier::create)
                .expectNextMatches(apod1 -> apod1.getTitle().equals("title"))
                .verifyComplete();
    }
    @Test
    void update() {
        template.update(Apod.class)
                                  .inTable("apod")
                                  .matching(Query.query(Criteria.where("title").is("title")))
                                  .apply(Update.update("copyright", "cheyen"))
                      .thenMany(apodRepository.findAll())
                      .as(StepVerifier::create)
                      .expectNextCount(2)
                      .verifyComplete();

//        apodRepository.setNewTitleFor((long)56, "che")
//                .thenMany(apodRepository.findAll())
//                .as(StepVerifier::create)
//                .expectNextMatches(c->c.getTitle().equals("che"))
//                .verifyComplete();


//        Mono.from(factory.create())
//                .flatMapMany(connection -> connection
//                        .createStatement("UPDATE apod SET title = $1 where title = $2")
//                        .bind("$1", "che4444")
//                        .bind("$2", "che")
//                        .execute())
////                .thenMany(apodRepository.findAll())
//                .as(StepVerifier::create)
////                .expectNextMatches(e->e.getTitle().equals("che4444"))
//                .verifyComplete();


    }
    @Test
    void delete() {
//        apodRepository.deleteById((long)54)
//                .thenMany(apodRepository.findAll())
//                .as(StepVerifier::create)
//                .expectNextCount(0)
//                .verifyComplete();

        template.delete(Apod.class)
                        .from("apod")
                .matching(Query.query(Criteria.where("id").is((long)55)))
                .all()
                .thenMany(apodRepository.findAll())
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }
//    @AfterEach
    void after() {
        log.info("=== finish repo test ===");
        apodRepository.deleteAll().subscribe();
    }
}
