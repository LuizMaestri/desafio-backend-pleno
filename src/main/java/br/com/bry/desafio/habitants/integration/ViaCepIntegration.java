package br.com.bry.desafio.habitants.integration;

import br.com.bry.desafio.habitants.config.IntegrationConfig;
import br.com.bry.desafio.habitants.exception.IntegrationException;
import br.com.bry.desafio.habitants.model.Address;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Locale;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ViaCepIntegration {
    WebClient client;
    IntegrationConfig config;
    MessageSource messageSource;

    public Mono<Address> getAddress(String cep){
        return client.get()
            .uri(
                uriBuilder ->
                    uriBuilder.pathSegment(cep, config.getFormat())
                        .build()
            ).retrieve()
            .onStatus(
                HttpStatus::isError,
                response ->
                    Mono.error(
                        new IntegrationException(
                            messageSource.getMessage(
                                "integration.invalid.cep",
                                    new Object[]{ cep },
                                    Locale.getDefault()
                            )
                        )
                    )
            ).bodyToMono(Address.class)
            .timeout(
                    Duration.ofMinutes(1),
                    Mono.error(
                            new HttpTimeoutException(
                                    messageSource.getMessage(
                                            "integration.timeout",
                                            null,
                                            Locale.getDefault()
                                    )
                            )
                    )
            ).filter(address -> address.getCity() != null)
            .switchIfEmpty(
                Mono.error(
                    new IntegrationException(
                        messageSource.getMessage(
                            "integration.not-found.cep",
                            new Object[]{ cep },
                            Locale.getDefault()
                        )
                    )
                )
            );
    }
}
