package com.r2dbctest.config;

import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class WebClientConfiguration {
    @Value("${api.url}")
    private String apiUrl;
    @Value("${webclient.connect-timeout}")
    private int connectTimeout;
    @Value("${webclient.write-timeout}")
    private int writeTimeout;
    @Value("${webclient.read-timeout}")
    private int readTimeout;
    @Value("${webclient.byte-count}")
    private int byteCount;

    @Bean
    public WebClient webClient() throws FileNotFoundException {
        HttpClient httpClient = HttpClient.create()
                                          .wiretap("org.springframework.web.reactive.function.client.ExchangeFunctions", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                                          .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                                          .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                                                                 .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)));

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(byteCount);
                })
                .build();

        return WebClient.builder()
                .baseUrl(apiUrl + new ApodCredentialProvider().getKey())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies)
                .build();
    }
}
