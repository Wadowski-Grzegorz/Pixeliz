package com.example.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
            contact = @Contact(
                    name = "If you have any issue please do not contact me"
            ),
            description = "Documentation for Spring Security",
            title = "Pixeliz"
    ),
    servers = {
        @Server(
                description = "Local",
                url = "http://localhost:9090"
        )
    }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authorization",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
