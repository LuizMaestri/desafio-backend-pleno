package bry.test.habitants.resources;

import bry.test.habitants.dto.HabitantDto;
import bry.test.habitants.model.Address;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Collections;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor(onConstructor = @__(@Autowired))
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