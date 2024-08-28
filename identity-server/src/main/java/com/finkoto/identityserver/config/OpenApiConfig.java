package com.finkoto.identityserver.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.keycloak.OAuth2Constants;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                contact = @Contact(name = "Blueprint Springboot",
                        email = "cihankara632@gmail.com"),
                description = "OpenApi document for spring security",
                title = "OpenApi specification - Blueprint",
                version = "1.0",
                license = @License(
                        name = "Blueprint-Springboot"
                ),
                termsOfService = "Terms of service"

        ),
        servers = {@Server(
                description = "Local ENV",
                url = "http://localhost:${server.port}${server.servlet.context-path}"
        )
        },
        security = {
                @SecurityRequirement(name = "password")
        }

)

@Configuration
public class OpenApiConfig {


    @Value("${springdoc.oAuthFlow.authorizationUrl}")
    private String authorizationUrl;

    @Value("${springdoc.oAuthFlow.tokenUrl}")
    private String tokenUrl;

    @Bean
    public GroupedOpenApi version1() {
        return GroupedOpenApi.builder()
                .group("identity server Rest API v1.0")
                .pathsToMatch("/v1/**")
                .addOpenApiCustomizer(setDocumentation("1.0"))
                .build();
    }


    private OpenApiCustomizer setDocumentation(String version) {
        return openApi -> {
            openApi
                    .info(new Info().title("REST API endpoints for identity Server").description("Use the REST API to interact with identity Server.").version(version))
                    .getPaths().values().stream().flatMap(pathItem -> pathItem.readOperations().stream())
                    .forEach(
                            operation ->
                                    operation.addParametersItem(new HeaderParameter().schema(new StringSchema()._default("tr")).name("Accept-Language"))
                    );
            final OAuthFlow password = new OAuthFlow();
            password.tokenUrl(tokenUrl);
            password.scopes(new Scopes());
            final OAuthFlows oAuthFlows = new OAuthFlows();
            oAuthFlows.password(password);
            final SecurityScheme securityScheme = new SecurityScheme()
                    .type(SecurityScheme.Type.OAUTH2)
                    .name(OAuth2Constants.PASSWORD)
                    .scheme(OAuth2Constants.PASSWORD)
                    .flows(oAuthFlows);
            openApi.getComponents().addSecuritySchemes("password", securityScheme);
        };
    }
}
