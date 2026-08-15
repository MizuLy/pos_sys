package com.example.pos_sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  private static final String SCHEME_NAME = "bearerAuth";

  @Bean
  public OpenAPI posOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("POS System API")
            .description("Point of Sale backend. Register returns a JWT; use it via the Authorize button.")
            .version("1.0.0"))
        .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
        .components(new Components().addSecuritySchemes(SCHEME_NAME,
            new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("Enter: Bearer <your-token>")));
  }
}
