package com.finkoto.identityserver.client;

import com.finkoto.identityserver.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "keycloakUserClient", url = "${keycloak.auth-server-url}/admin/realms/${keycloak.realm}")
public interface KeycloakUserClient {
    @PostMapping(value = "/protocol/openid-connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDto token(Map<String, ?> form);

    @GetMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<UserResponseDto> getAllUsers(@RequestHeader("Authorization") String token);

    @GetMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserResponseDto getByIdUser(@RequestHeader("Authorization") String token, @PathVariable String id);

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CreateUserDto> createUser(@RequestBody Map<String, ?> user,
                                             @RequestHeader("Authorization") String token);

    @DeleteMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CreateUserDto> deleteUser(@RequestHeader("Authorization") String token, @PathVariable String id);

    @PutMapping (value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserUpdateDto> updateUser(@RequestBody Map<String, ?> user, @RequestHeader("Authorization") String token, @PathVariable String id);

    @PutMapping(value = "/users/{id}/reset-password",consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<UserPasswordDto> resetPassword( @RequestHeader("Authorization") String token, @PathVariable String id,@RequestBody Map<String, ?> user);

}


