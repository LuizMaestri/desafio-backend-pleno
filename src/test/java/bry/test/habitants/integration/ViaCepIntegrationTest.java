package bry.test.habitants.integration;

import bry.test.habitants.exception.IntegrationException;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class ViaCepIntegrationTest {

    ViaCepIntegration integration;

    @Test
    void getAddress() {
        StepVerifier.create(integration.getAddress("88080180"))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void validateAddress() {
        StepVerifier.create(integration.getAddress("88080180"))
            .assertNext(address -> {
                assertEquals("Rua José de Alencar", address.getStreet());
                assertEquals("Capoeiras", address.getNeighborhood());
                assertEquals("Florianópolis", address.getCity());
                assertEquals("SC", address.getState());
            }).verifyComplete();
    }

    @Test
    void getAddressNullCep(){
        StepVerifier.create(integration.getAddress(null)).verifyError(IntegrationException.class);
    }

    @Test
    void getAddressInvalidCepSizeLess() {
        StepVerifier.create(integration.getAddress("2133")).verifyError(IntegrationException.class);
    }

    @Test
    void getAddressInvalidCapSizeMore() {
        StepVerifier.create(integration.getAddress("213345678780")).verifyError(IntegrationException.class);
    }

    @Test
    void getAddressInvalidCepNumber() {
        StepVerifier.create(integration.getAddress("11111111")).verifyError(IntegrationException.class);
    }
}