package com.finkoto.chargestation.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @Contact(name = "Finkoto Fake Charge Central Simulation",
                        email = "cihankara632@gmail.com"),
                description = "Finkoto Fake Charge Point Simulation",
                title = "Charge Central Restful API",
                version = "1.0",
                license = @License(name = "Finkoto")
        ),
        servers = {@Server(
                description = "Local ENV",
                url = "http://localhost:${server.port}${server.servlet.context-path}"
        )}
)
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Central System")
                        .version("1.0")
                        .description("Central System REST API"));
    }

    @Bean
    public GroupedOpenApi version1() {
        return GroupedOpenApi.builder()
                .group("Charge Point Rest API v1.0")
                .pathsToMatch("/csm/v1/charging-sessions/**","/csm/v1/charge-points/**","/csm/v1/connectors/**","/csm/v1/charge-HardwareSpec-connector")
                .addOpenApiCustomizer(setDocumentation("1.0"))
                .build();
    }

    private OpenApiCustomizer setDocumentation(String version) {
        return openApi -> {
            openApi.info(new Info()
                            .title("REST API endpoints for Charge Point Server")
                            .description("Use the REST API to interact with Charge Point Server.")
                            .version(version))
                    .getPaths().values().stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream())
                    .forEach(operation -> operation.addParametersItem(
                            new HeaderParameter().schema(new StringSchema()._default("tr")).name("Accept-Language")));
        };
    }
}
