package com.microservice.gateway.configuration;

import com.microservice.gateway.repository.IdentityClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class IdentityClientConfig {

    @Value("${app.identity.base-url}")
    String baseUrl;

    @Bean
    WebClient identityWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    IdentityClient identityClient(WebClient identityWebClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(identityWebClient))
                .build();

        return httpServiceProxyFactory.createClient(IdentityClient.class);
    }
}
