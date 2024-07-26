package com.finkoto.identityserver.client;

import com.finkoto.identityserver.dto.CreateTokenDto;
import com.finkoto.identityserver.dto.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "keycloak", url = "${keycloak.auth-server-url}")
public interface KeycloakAdminClient {
    @PostMapping("/realms/${keycloak.realm}/protocol/openid-connect/token")
    TokenResponseDto createToken(@RequestBody CreateTokenDto dto);

}