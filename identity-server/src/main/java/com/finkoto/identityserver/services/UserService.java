package com.finkoto.identityserver.services;

import com.finkoto.identityserver.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
}
