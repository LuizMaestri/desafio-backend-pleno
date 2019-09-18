package bry.test.habitants.service;

import bry.test.habitants.dto.HabitantDto;
import bry.test.habitants.model.Address;
import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class HabitantServiceTest {

    HabitantService service;
    Faker faker;

    @Test
    void createSuccess(){
        StepVerifier.create(
            service.createHabitant(
                HabitantDto.builder()
                    .name(
                        faker.name()
                            .fullName()
                    ).code(
                        faker.number()
                            .digits(6)
                    ).addresses(
                        List.of(
                            new Address()
                                .withNumber(
                                    faker.number()
                                        .numberBetween(100, 999)
                                ).withPostalCode("88080180")
                        )
                    ).build()
            )
        ).expectNextCount(1)
           .verifyComplete();
    }

    @Test
    void createSuccessWithoutAddress(){
        StepVerifier.create(
            service.createHabitant(
                HabitantDto.builder()
                    .name(
                        faker.name()
                            .fullName()
                    ).code(
                       faker.number()
                            .digits(6)
                    ).build()
            ).cast(HabitantDto.class)
        ).assertNext(
            habitantDto ->
                assertTrue(
                    habitantDto.getAddresses()
                        .isEmpty()
                )
        ).verifyComplete();
    }

    @Test
    void createErrorHabitantData() {
        StepVerifier.create(
            service.createHabitant(
                HabitantDto.builder()
                    .code(
                        faker.number()
                            .digits(6)
                   ).build()
            )
        ).verifyError();
    }

    @Test
    void createErrorAddressData(){
        StepVerifier.create(
            service.createHabitant(
                HabitantDto.builder()
                    .name(
                        faker.name()
                            .fullName()
                    ).code(
                        faker.number()
                            .digits(6)
                    ).addresses(
                        List.of(
                            new Address()
                                .withNumber(
                                    faker.number()
                                        .numberBetween(100, 999)
                                ).withPostalCode("11111111")
                        )
                    ).build()
            )
        ).verifyError();
    }

    @Test
    void getHabitant(){
        StepVerifier.create(
            service.createHabitant(
                HabitantDto.builder()
                    .name(
                        faker.name()
                            .fullName()
                    ).code(
                        faker.number()
                            .digits(6)
                    ).addresses(
                        List.of(
                            new Address()
                                .withNumber(
                                    faker.number()
                                        .numberBetween(100, 999)
                                ).withPostalCode("88080180")
                        )
                    ).build()
            ).cast(HabitantDto.class)
                .flatMap(
                    habitantDto -> service.getHabitant(habitantDto.getCode())
                )
        ).expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getHabitantError() {
        StepVerifier.create(
            service.getHabitant("553215231351345")
        ).verifyError(ResponseStatusException.class);
    }

    @Test
    void deleteHabitant() {
        StepVerifier.create(
            service.createHabitant(
                HabitantDto.builder()
                    .name(
                        faker.name()
                            .fullName()
                    ).code(
                       faker.number()
                            .digits(6)
                    ).addresses(
                        List.of(
                            new Address()
                                .withNumber(
                                    faker.number()
                                            .numberBetween(100, 999)
                                ).withPostalCode("88080180")
                        )
                ).build()
            ).cast(HabitantDto.class)
                .flatMap(
                    habitantDto -> service.deleteHabitant(habitantDto.getCode())
                )
        ).expectNext(true)
            .verifyComplete();
    }

    @Test
    void deleteHabitantError() {
        StepVerifier.create(
            service.deleteHabitant("553215231351345")
        ).verifyError(ResponseStatusException.class);
    }

}