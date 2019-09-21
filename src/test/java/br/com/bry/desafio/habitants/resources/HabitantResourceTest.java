package br.com.bry.desafio.habitants.resources;

import br.com.bry.desafio.habitants.dto.HabitantDto;
import br.com.bry.desafio.habitants.model.Address;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@AutoConfigureWebTestClient(timeout = "120000")
class HabitantResourceTest {

    static final String BASE_URI = "habitantes";
    WebTestClient client;
    Faker faker;

    @Test
    void createSuccess(){
        client.post()
            .uri(
                uriBuilder ->
                    uriBuilder.pathSegment(BASE_URI)
                        .build()
            ).body(
                BodyInserters.fromObject(
                    HabitantDto.builder()
                        .name(
                            faker.name()
                                .fullName()
                        ).code(
                            faker.number()
                                .digits(6)
                        ).addresses(
                            Collections.singletonList(
                                new Address()
                                    .withPostalCode("88080180")
                                    .withNumber(
                                        faker.number()
                                            .numberBetween(100, 500)
                                    )
                            )
                        )
                )
            ).exchange()
            .expectStatus()
            .isCreated();
    }

    @Test
    void createSuccessNotAddress() {
        client.post()
            .uri(
                uriBuilder ->
                    uriBuilder.pathSegment(BASE_URI)
                        .build()
            ).body(
                BodyInserters.fromObject(
                    HabitantDto.builder()
                        .name(
                            faker.name()
                                .fullName()
                        ).code(
                        faker.number()
                            .digits(6)
                    )
                )
            ).exchange()
            .expectStatus()
            .isCreated();
    }
}