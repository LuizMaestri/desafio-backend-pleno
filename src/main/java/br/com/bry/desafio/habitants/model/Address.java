package br.com.bry.desafio.habitants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @JsonIgnore
    Long id;
    @NotNull
    @NotEmpty
    @Size(min = 8, max = 8)
    @JsonProperty("codigoPostal")
    String postalCode;
    @JsonProperty("logradouro")
    String street;
    @JsonProperty("bairro")
    String neighborhood;
    @JsonProperty("numero")
    @NotNull
    Integer number;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("complemento")
    String complement;
    @JsonProperty("localidade")
    String city;
    @JsonProperty("uf")
    String state;
    @JsonIgnore
    String habitantCode;
}
