package bry.test.habitants.repository;

import bry.test.habitants.model.Address;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AddressRepository extends R2dbcRepository<Address, Long> {
    @Query("select * from address where habitant_code = :habitantCode")
    Flux<Address> findAddressByHabitantCod(String habitantCode);
    @Query("delete from address where habitant_code = :habitantCode")
    Mono<Void> deleteAddressesByHabitantCod(String habitantCode);
}
