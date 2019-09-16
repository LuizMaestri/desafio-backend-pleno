package br.com.bry.desafio.habitants.dto;

import br.com.bry.desafio.habitants.model.Address;
import br.com.bry.desafio.habitants.model.Habitant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder(builderClassName = "Builder")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = HabitantDto.Builder.class)
public class HabitantDto {

    @JsonProperty("codigo")
    @NotNull
    @NotEmpty
    String code;
    @JsonProperty("nome")
    @NotNull
    @NotEmpty
    String name;
    @JsonProperty("enderecos")
    List<@Valid Address> addresses;

    @JsonIgnore
    public Habitant getHabitant(){
        return Habitant.builder()
            .code(code)
            .name(name)
            .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder{
        @JsonProperty("codigo")
        String code;
        @JsonProperty("nome")
        String name;
        @JsonProperty("enderecos")
        List<Address> addresses;
    }
}
