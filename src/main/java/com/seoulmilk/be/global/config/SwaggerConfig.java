package com.seoulmilk.be.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(title = "SeoulMilk API 명세서",
                description = "SeoulMilk API 명세서",
                version = "v1"
        ),
        servers = @Server(url = "/api", description = "Default Server URL")
)

@Configuration
public class SwaggerConfig {

    // Swagger에 JWT Authorization 추가
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("BearerAuth", securityScheme))
                .security(Collections.singletonList(securityRequirement));
    }
}