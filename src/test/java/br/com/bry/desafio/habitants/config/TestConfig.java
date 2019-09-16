package br.com.bry.desafio.habitants.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class TestConfig {

    @Bean
    public Faker faker(){
        return Faker.instance(Locale.getDefault());
    }
}
