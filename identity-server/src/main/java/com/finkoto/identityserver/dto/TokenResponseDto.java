package com.finkoto.identityserver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter

public class TokenResponseDto {




    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private String rexpiresIn;
    @JsonProperty("refresh_expires_in")
    private String refreshExpiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("session_state")
    private String sessionState;

    public String getAccessToken() {
        return tokenType+" "+accessToken;
    }


}
