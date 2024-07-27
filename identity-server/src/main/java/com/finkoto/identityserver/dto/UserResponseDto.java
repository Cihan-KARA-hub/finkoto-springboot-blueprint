package com.finkoto.identityserver.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserResponseDto {
    private String id;
    private String username;
    private String email;
    private boolean enabled;
}