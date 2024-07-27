package com.finkoto.identityserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDto {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private int expires_in;
}
