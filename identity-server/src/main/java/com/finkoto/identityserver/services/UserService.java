package com.finkoto.identityserver.services;


import com.finkoto.identityserver.dto.CreateTokenDto;
import com.finkoto.identityserver.dto.TokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Value("${keycloak.token-uri}")
    private String tokenUri;

    @Value("${keycloak.client-id}")
    private String clientId;



    public TokenResponseDto createToken(CreateTokenDto dto) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("username", dto.getUserName());
        params.put("password", dto.getPassword());
        params.put("grant_type", "password");
        params.put("client_id", clientId);


        TokenResponseDto response = restTemplate.postForObject(tokenUri, params, TokenResponseDto.class);
        return response;
    }

}
