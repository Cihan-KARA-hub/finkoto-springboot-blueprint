package com.finkoto.identityserver.client;


import com.finkoto.identityserver.dto.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.PostMapping;



import java.util.Map;

@FeignClient(name = "keycloakAdminClient", url = "${keycloak.auth-server-url}/realms/${keycloak.realm}")
public interface KeycloakAdminClient {
    @PostMapping(value = "/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDto token(Map<String, ?> form);


}
