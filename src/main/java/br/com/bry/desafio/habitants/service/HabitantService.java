package br.com.bry.desafio.habitants.service;

import br.com.bry.desafio.habitants.dto.HabitantDto;
import br.com.bry.desafio.habitants.exception.IntegrationException;
import br.com.bry.desafio.habitants.integration.ViaCepIntegration;
import br.com.bry.desafio.habitants.model.Address;
import br.com.bry.desafio.habitants.model.Habitant;
import br.com.bry.desafio.habitants.repository.AddressRepository;
import br.com.bry.desafio.habitants.repository.HabitantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Validation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HabitantService {

    HabitantRepository habitantRepository;
    AddressRepository addressRepository;
    ViaCepIntegration integration;

    public Flux<HabitantDto> findHabitants(){
        return habitantRepository.findAll()
            .flatMap(this::getAddresses);
    }

    public Mono<HabitantDto> getHabitant(String cod) {
        return getOrNotFound(cod)
                .flatMap(this::getAddresses);
    }

    @Transactional
    public Mono<Object> createHabitant(HabitantDto habitant){
        return validate(habitant)
            .flatMap(
                valid ->
                    habitantRepository.getByCod(habitant.getCode())
                        .flatMap(
                            find -> Mono.error(new ResponseStatusException(HttpStatus.CONFLICT))
                        ).switchIfEmpty(
                            habitantRepository.save(habitant.getHabitant())
                                .flatMap(saved -> mapAddress(habitant, saved))
                        )
            );
    }

    @Transactional
    public Mono<HabitantDto> updateHabitant(HabitantDto habitant, String cod) {
        return validate(habitant)
            .flatMap(
                valid ->
                    getOrNotFound(cod)
                        .flatMap(
                            find ->
                                habitantRepository.save(
                                    habitant.getHabitant()
                                        .withId(find.getId())
                                )
                        ).flatMap(
                            saved ->
                                addressRepository.deleteAddressesByHabitantCod(cod)
                                    .then(mapAddress(habitant, saved))
                        )
            );
    }

    public Mono<Boolean> deleteHabitant(String cod) {
        return getOrNotFound(cod).flatMap(
            habitant ->
                addressRepository.deleteAddressesByHabitantCod(habitant.getCode())
                    .then(
                        habitantRepository.delete(habitant)
                    ).thenReturn(true)
        );
    }

    private HabitantDto build(Habitant habitant, List<Address> addresses){
        return HabitantDto.builder()
            .code(habitant.getCode())
            .name(habitant.getName())
            .addresses(addresses)
            .build();
    }

    private Mono<HabitantDto> getAddresses(Habitant habitant){
        return addressRepository.findAddressByHabitantCod(habitant.getCode())
            .collectList()
            .map(addresses -> build(habitant, addresses));
    }

    private Mono<Habitant> getOrNotFound(String cod){
        return habitantRepository.getByCod(cod)
            .switchIfEmpty(
                Mono.error(
                    new ResponseStatusException(HttpStatus.NOT_FOUND)
                )
            );
    }

    private Mono<HabitantDto> mapAddress(HabitantDto habitant, Habitant saved) {
        return Flux.fromIterable(
                Optional.ofNullable(
                    habitant.getAddresses()
                ).orElse(Collections.emptyList())
            ).parallel()
            .runOn(Schedulers.parallel())
            .flatMap(address ->
                integration.getAddress(address.getPostalCode())
                    .map(
                        integratedAddress ->
                            integratedAddress.withHabitantCode(saved.getCode())
                                .withPostalCode(address.getPostalCode())
                                .withNumber(address.getNumber())
                                .withComplement(address.getComplement())
                    ).onErrorMap(
                        throwable -> throwable instanceof IntegrationException,
                        throwable -> {
                            log.error(throwable.getMessage(), throwable);
                            return new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                throwable.getMessage(),
                                throwable
                            );
                        }
                    )
            ).sequential()
            .collectList()
            .flatMapMany(addressRepository::saveAll)
            .collectList()
            .map(addresses -> build(saved, addresses));
    }

    private Mono<Boolean> validate(HabitantDto habitant){
        return Mono.just(
            Validation.buildDefaultValidatorFactory()
                .getValidator()
                .validate(habitant)
                .size() == 0
        ).filter(Boolean::booleanValue)
            .switchIfEmpty(
                Mono.error(
                    new ResponseStatusException(HttpStatus.BAD_REQUEST)
                )
            );
    }
}
