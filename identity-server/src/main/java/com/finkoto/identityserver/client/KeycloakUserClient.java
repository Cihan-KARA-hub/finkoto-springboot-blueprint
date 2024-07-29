package com.finkoto.identityserver.client;

import com.finkoto.identityserver.dto.TokenResponseDto;
import com.finkoto.identityserver.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloakClient", url = "${keycloak.auth-server-url}/admin/realms/${keycloak.realm}")
public interface KeycloakUserClient {
    @PostMapping(value = "/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDto token(Map<String, ?> form) ;

    @GetMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserResponseDto> getAllUsers(@RequestHeader("Authorization") String token) ;

    @GetMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
     UserResponseDto getByIdUser(@RequestHeader("Authorization" ) String token ,@PathVariable String id ) ;

}
