package com.r2dbctest.config;

import java.time.Duration;

import org.mariadb.r2dbc.MariadbConnectionConfiguration;
import org.mariadb.r2dbc.MariadbConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;

/**
 * Project : r2dbc-test
 * Created by IntelliJ IDEA
 * Developer : cheyenneshin
 * Date Time : 2022/08/30 4:27 PM
 * Summary :
 **/
@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "com.r2dbctest.apod", entityOperationsRef = "postgresR2dbcEntityOperations")
public class PostgresConfiguration {
    @Bean
    @Qualifier("postgresql")
    public ConnectionFactory postgresConnectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder().host("localhost").port(5432).username("testuser1").password("1234").database("apod").build());

    }
    @Bean
    public R2dbcEntityOperations postgresR2dbcEntityOperations(@Qualifier("postgresql") ConnectionFactory connectionFactory) {
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, PostgresDialect.INSTANCE);
    }

    @Bean
    public ConnectionFactoryInitializer postgresInitializer(@Qualifier("postgresql") ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema_postgres.sql")));
        return initializer;
    }

    //커넥션풀은 성능 오버헤드 저하 방지를 위해 데이터베이스 커넥션을 재사용
    @Bean
    public ConnectionPool PostgresConnectionPool(@Qualifier("postgresql") ConnectionFactory connectionFactory) {
        ConnectionPoolConfiguration poolConf = ConnectionPoolConfiguration.builder()
                                                                          .connectionFactory(connectionFactory)
                                                                          .maxIdleTime(Duration.ofMillis(1000))
                                                                          .initialSize(20)
                                                                          .maxSize(50)
                                                                          .build();
        return new ConnectionPool(poolConf);
    }


    @Bean
    public ReactiveTransactionManager postgresTransactionManager(@Qualifier("postgresql") ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
    @Bean
    public TransactionalOperator postgresOperator(@Qualifier("postgresTransactionManager") ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }
}
