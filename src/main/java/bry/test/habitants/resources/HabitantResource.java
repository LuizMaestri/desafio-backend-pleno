package bry.test.habitants.resources;

import bry.test.habitants.dto.HabitantDto;
import bry.test.habitants.model.Habitant;
import bry.test.habitants.service.HabitantService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
@AllArgsConstructor(onConstructor = @__(@Autowired))
@FieldDefaults(makeFinal = true)
public class HabitantResource {

    HabitantService service;

    @Bean
    RouterFunction<ServerResponse> routerFunction(){
        String path = "habitantes";
        String pathVariable = "code";
        String pathCod = path + "/{" + pathVariable + "}";
        return route()
            .GET(
                path,
                req ->
                    ok().body(service.findHabitants(), HabitantDto.class)
            ).GET(
                pathCod,
                req ->
                    ok().body(
                        service.getHabitant(
                            req.pathVariable(pathVariable)
                        ),
                        Habitant.class
                    )
            ).POST(
                path,
                contentType(APPLICATION_JSON),
                req ->
                    req.bodyToMono(HabitantDto.class)
                        .flatMap(service::createHabitant)
                        .flatMap(
                            habitant ->
                                created(
                                    UriComponentsBuilder.newInstance()
                                        .pathSegment(
                                            path,
                                            ((HabitantDto) habitant).getCode()
                                        ).build()
                                        .toUri()
                                ).body(BodyInserters.fromObject(habitant))
                        )
            ).PUT(
                pathCod,
                contentType(APPLICATION_JSON),
                req ->
                    req.bodyToMono(HabitantDto.class)
                        .flatMap(habitant -> service.updateHabitant(habitant, req.pathVariable(pathVariable)))
                        .flatMap(savedHabitant -> noContent().build())
            ).DELETE(
                pathCod,
                    req ->
                        service.deleteHabitant(req.pathVariable(pathVariable))
                            .flatMap(aBoolean -> noContent().build())
            ).build();
    }
}
