package com.finkoto.identityserver.services;


import com.finkoto.identityserver.api.dto.*;
import com.finkoto.identityserver.api.client.KeycloakAdminClient;
import com.finkoto.identityserver.api.client.KeycloakUserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@Service
public class UserRefreshTokenService {
    private final KeycloakUserClient keycloakUserClient;
    private final KeycloakAdminClient keycloakAdminClient;
    @Value("${keycloak.admin-username}")
    private String admin_username;
    @Value("${keycloak.admin-password}")
    private String admin_password;

    public TokenResponseDto getToken(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", "admin-cli");
        body.put("username", admin_username);
        body.put("password", admin_password);
        body.put("grant_type", "password");

        return keycloakUserClient.token(body);
    }

    public TokenResponseDto getUserToken(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("client_id", "blueprint-springboot");
        body.put("username", username);
        body.put("password",password);
        body.put("grant_type", "password");
        return keycloakUserClient.token(body);
    }

    public List<UserResponseDto> getAllUsers() {
        TokenResponseDto token = getToken(admin_username, admin_password);
        return keycloakAdminClient.getAllUsers(token.getRefreshToken());
    }

    public UserResponseDto getByIdUser(String id) {
        TokenResponseDto token = getToken(admin_username, admin_password);
        String accessToken = token.getRefreshToken();
        return keycloakUserClient.getByIdUser(accessToken, id);
    }

    public void createUser(CreateUserDto createUserDto) {
        TokenResponseDto token = getToken(admin_username, admin_password);
        String acessToken = token.getRefreshToken();
        Map<String, Object> body = new HashMap<>();
        body.put("enabled", Boolean.parseBoolean(String.valueOf(createUserDto.isEnabled())));
        body.put("username", createUserDto.getUsername());
        body.put("firstName", createUserDto.getFirstName());
        body.put("lastName", createUserDto.getLastName());
        body.put("email", createUserDto.getEmail());
        keycloakAdminClient.createUser(body, acessToken);
    }

    public void userDelete(String id) {
        TokenResponseDto token = getToken(admin_username, admin_password);
        String accessToken = token.getRefreshToken();
        keycloakAdminClient.deleteUser(accessToken, id);
    }

    public void updateUser(UserUpdateDto userUpdateDto, String id) {
        TokenResponseDto token = getToken(admin_username, admin_password);
        String accessToken = token.getRefreshToken();
        Map<String, Object> body = new HashMap<>();
        body.put("enabled", Boolean.parseBoolean(String.valueOf(userUpdateDto.isEnabled())));
        body.put("username", userUpdateDto.getUsername());
        body.put("firstName", userUpdateDto.getFirstName());
        body.put("lastName", userUpdateDto.getLastName());
        body.put("email", userUpdateDto.getEmail());

        // Logging to check if ID and body content are correct
        System.out.println("Updating user with ID: " + id);
        System.out.println("Request body: " + body);

        keycloakAdminClient.updateUser(body, accessToken, id);
    }

    public void resetPassword(UserPasswordDto userPasswordDto, String id) {
        TokenResponseDto token = getToken(admin_username, admin_password);
        String accessToken = token.getRefreshToken();
        Map<String, Object> body = new HashMap<>();
        body.put("type", userPasswordDto.getType());
        body.put("value", userPasswordDto.getValue());
        body.put("temporary", Boolean.parseBoolean(String.valueOf(userPasswordDto.isTemporary())));

        System.out.println("req " + userPasswordDto.getValue());

        keycloakAdminClient.resetPassword(accessToken, id, body);
    }

}
