package com.finkoto.identityserver.api.client;


import com.finkoto.identityserver.api.dto.TokenResponseDto;
import com.finkoto.identityserver.api.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "keycloakAdminClient", url = "${keycloak.auth-server-url}/realms/master")
public interface KeycloakUserClient {
    @PostMapping(value = "/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDto token(Map<String, ?> form);

    @GetMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserResponseDto getByIdUser(@RequestHeader("Authorization") String token, @PathVariable String id);

}
