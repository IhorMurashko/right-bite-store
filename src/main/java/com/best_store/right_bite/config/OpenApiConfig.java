package com.best_store.right_bite.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Right Bite Store API",
                version = "0.0.1-SNAPSHOT",
                description = "Backend API for Right Bite Online Store. Built using Spring Boot.",
                contact = @Contact(
                        name = "Ihor Murashko",
                        email = "i.murashko0911@gmail.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local Dev Server"),
                @Server(url = "https://right-bite-store.onrender.com", description = "Test Server")
        }
)
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}
