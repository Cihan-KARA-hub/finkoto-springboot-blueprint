package com.finkoto.chargestation.api.config;

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
                contact = @Contact(name = "Finkoto Charge Point Central",
                        email = "cihankara632@gmail.com"),
                description = "Finkoto Charge Point Central API",
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
                        .title("Charge Station Server")
                        .version("1.0")
                        .description("Charge System REST API"));
    }

    @Bean
    public GroupedOpenApi version1() {
        return GroupedOpenApi.builder()
                .group(" Charge Station Server Rest API v1.0")
                .addOpenApiCustomizer(setDocumentation("1.0"))
                .build();
    }

    private OpenApiCustomizer setDocumentation(String version) {
        return openApi -> {
            openApi.info(new Info()
                            .title("REST API endpoints for  Charge Station Server")
                            .description("Use the REST API to interact with Mock Charge Station Server.")
                            .version(version))
                    .getPaths().values().stream()
                    .flatMap(pathItem -> pathItem.readOperations().stream())
                    .forEach(operation -> operation.addParametersItem(
                            new HeaderParameter().schema(new StringSchema()._default("tr")).name("Accept-Language")));
        };
    }
}
