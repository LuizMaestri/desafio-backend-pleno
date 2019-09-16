package bry.test.habitants.repository;

import bry.test.habitants.model.Habitant;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import reactor.core.publisher.Mono;

public interface HabitantRepository extends R2dbcRepository<Habitant, Long> {
    @Query("select * from habitant where code = :code")
    Mono<Habitant> getByCod(String code);
}
