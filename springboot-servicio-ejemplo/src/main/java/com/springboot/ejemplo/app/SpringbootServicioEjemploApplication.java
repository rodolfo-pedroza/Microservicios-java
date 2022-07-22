package com.springboot.ejemplo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class SpringbootServicioEjemploApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioEjemploApplication.class, args);
	}

	   @Bean
	    public OpenAPI customOpenAPI() {
	        return new OpenAPI()
	                .components(new Components())
	                .info(new Info().title("Cliente API").description(
	                		"Esta API permite operaciones CRUD en un cliente")
	                		.version("1.0")
	                		);
	    }
}
