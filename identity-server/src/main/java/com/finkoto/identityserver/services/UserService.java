package com.finkoto.identityserver.services;

import com.finkoto.identityserver.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.users-uri}")
    private  String url;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public String getToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);
        body.add("grant_type", "password");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return Objects.requireNonNull(response.getBody()).get("access_token").toString();
            } else {
                logger.error("Failed to retrieve token: " + response.getStatusCode());
                throw new RuntimeException("Failed to retrieve token");
            }
        } catch (Exception e) {
            logger.error("Exception occurred while retrieving token: " + e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve token", e);
        }
    }

    public List<UserResponseDto> getAllUser(String username, String password) {
        String token = getToken(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UserResponseDto[]> responseEntity =
                    restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserResponseDto[].class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                UserResponseDto[] users = responseEntity.getBody();
                if (users != null) {
                    return Arrays.asList(users);
                } else {
                    logger.error("Failed to fetch users: response body is null");
                    throw new RuntimeException("Failed to fetch users: response body is null");
                }
            } else {
                logger.error("Failed to fetch users: " + responseEntity.getStatusCode());
                throw new RuntimeException("Failed to fetch users: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching users: " + e.getMessage(), e);
            throw new RuntimeException("Failed to fetch users", e);
        }
    }
    public UserResponseDto getUserById(String userId, String username, String password) {
        String token = getToken(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String userUrl = "http://localhost:8180/admin/realms/master/users/"+ userId; // Construct the URL
            ResponseEntity<UserResponseDto> responseEntity =
                    restTemplate.exchange(userUrl, HttpMethod.GET, requestEntity, UserResponseDto.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                logger.error("Failed to fetch user by ID: " + responseEntity.getStatusCode());
                throw new RuntimeException("Failed to fetch user by ID: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching user by ID: " + e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user by ID", e);
        }
    }

    public UserResponseDto createUser(UserResponseDto userDto, String username, String password) {
        String token = getToken(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserResponseDto> requestEntity = new HttpEntity<>(userDto, headers);


        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UserResponseDto> responseEntity =
                    restTemplate.exchange(url, HttpMethod.POST, requestEntity, UserResponseDto.class);

            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                return responseEntity.getBody();
            } else {
                logger.error("Failed to create user: " + responseEntity.getStatusCode());
                throw new RuntimeException("Failed to create user: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while creating user: " + e.getMessage(), e);
            throw new RuntimeException("Failed to create user", e);
        }
    }
    public void deleteUserById(String userId, String username, String password) {
        String token = getToken(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String deleteUserUrl = url + "/" + userId;
            ResponseEntity<Void> responseEntity =
                    restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, requestEntity, Void.class);

            if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
                logger.info("User with ID " + userId + " deleted successfully.");
            } else {
                logger.error("Failed to delete user: " + responseEntity.getStatusCode());
                throw new RuntimeException("Failed to delete user: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while deleting user: " + e.getMessage(), e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }
    public void updateUserById(String userId,  UserResponseDto userResponseDto, String username, String password) {
        String token = getToken(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("firstName", userResponseDto.getFirstName());
        updateFields.put("lastName", userResponseDto.getLastName());
        updateFields.put("username", userResponseDto.getUsername());
        updateFields.put("email", userResponseDto.getEmail());
        updateFields.put("true", userResponseDto.isEnabled());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(updateFields, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String updateUserUrl = url + "/" + userId;
            ResponseEntity<Void> responseEntity =
                    restTemplate.exchange(updateUserUrl, HttpMethod.PUT, requestEntity, Void.class);

            if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
                logger.info("User with ID " + userId + " updated successfully.");
            } else {
                logger.error("Failed to update user: " + responseEntity.getStatusCode());
                throw new RuntimeException("Failed to update user: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception occurred while updating user: " + e.getMessage(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }


}