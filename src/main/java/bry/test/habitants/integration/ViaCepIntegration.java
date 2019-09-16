package bry.test.habitants.integration;

import bry.test.habitants.config.IntegrationConfig;
import bry.test.habitants.exception.IntegrationException;
import bry.test.habitants.model.Address;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
            .filter(address -> address.getCity() != null)
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
