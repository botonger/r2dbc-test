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
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "com.r2dbctest.apod", entityOperationsRef = "mariadbR2dbcEntityOperations")
public class MariaDBConfiguration {

    @Bean
    @Qualifier("mariadb")
    public ConnectionFactory mariadbConnectionFactory() {
        MariadbConnectionConfiguration conf = MariadbConnectionConfiguration.builder().host("localhost").port(3306).username("root").password("1234").database("apod").build();

        return new MariadbConnectionFactory(conf);
    }
    @Bean
    public R2dbcEntityOperations mariadbR2dbcEntityOperations(@Qualifier("mariadb") ConnectionFactory connectionFactory) {
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        return new R2dbcEntityTemplate(databaseClient, MySqlDialect.INSTANCE);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(@Qualifier("mariadb") ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return initializer;
    }

    //커넥션풀은 성능 오버헤드 저하 방지를 위해 데이터베이스 커넥션을 재사용
    @Bean
    public ConnectionPool connectionPool(@Qualifier("mariadb") ConnectionFactory connectionFactory) {
        ConnectionPoolConfiguration poolConf = ConnectionPoolConfiguration.builder()
                                                                          .connectionFactory(connectionFactory)
                                                                          .maxIdleTime(Duration.ofMillis(1000))
                                                                          .initialSize(20)
                                                                          .maxSize(50)
                                                                          .build();
        return new ConnectionPool(poolConf);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(@Qualifier("mariadb") ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

//    @Bean //test용
//    public CommandLineRunner init(ApodRepository repository) {
//        return args -> {
//            repository.save(Testtest.builder().name("t").job("hj").age(33).build())
//                      .thenMany(repository.findAll())
//                      .subscribe(System.out::println);
//        };
//    }
}
