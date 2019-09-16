package br.com.bry.desafio.habitants.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegrationConfig {

    @Value("${integration.via-cep.url}")
    String url;
    @Getter
    @Value("${integration.via-cep.format}")
    String format;

    @Bean
    WebClient webClient(){
        return WebClient.create(url);
    }
}
