package com.finkoto.identityserver.services;

import com.finkoto.identityserver.client.KeycloakAdminClient;
import com.finkoto.identityserver.client.KeycloakUserClient;
import com.finkoto.identityserver.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Service
public class UserService {

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.users-uri}")
    private  String url;

    @Value("${keycloak.admin-username}")
    private  String admin_username;

    @Value("${keycloak.admin-password}")
    private  String admin_password;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final KeycloakAdminClient keycloakAdminClient;
    private  final KeycloakUserClient keycloakUserClient;


    public TokenResponseDto getToken(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", "admin-cli");
        body.put("username", admin_username);
        body.put("password", admin_password);
        body.put("grant_type", "password");

        TokenResponseDto  tokenResponse = keycloakAdminClient.token(body);
        return tokenResponse;
    }

    public TokenResponseDto  getUserToken(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", "blueprint-springboot");
        body.put("username", "cihan2");
        body.put("password", "123456");
        body.put("grant_type", "password");
        return keycloakAdminClient.token(body);
    }

    public List<UserResponseDto> getAllUsers() {
        TokenResponseDto token = getToken(admin_username,admin_password);
        token.getAccessToken();
              return keycloakUserClient.getAllUsers( token.getAccessToken());
    }

    public UserResponseDto getByIdUser(String id) {
        TokenResponseDto token = getToken(admin_username, admin_password);
        String accessToken = token.getAccessToken();
        return keycloakUserClient.getByIdUser(accessToken ,id);
    }
    public CreateUserDto createUserDto(CreateUserDto createUserDto){
        Map<String, String> body = new HashMap<>();
        body.put("enabled", "true");
        body.put("username",createUserDto.getUsername());
        body.put("firstName", createUserDto.getFirstName());
        body.put("lastName", createUserDto.getLastName());
        body.put("email", createUserDto.getEmail());
        return  keycloakAdminClient.createUser(body);

    }






}