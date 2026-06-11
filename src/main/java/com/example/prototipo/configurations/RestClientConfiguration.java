package com.example.prototipo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {
    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout((int) Duration.ofSeconds(10).toMillis());

        factory.setReadTimeout((int) Duration.ofSeconds(30).toMillis());

        return RestClient.builder()
                    .baseUrl("https://pncp.gov.br")
                .requestFactory(factory)
                .build();
    }
}