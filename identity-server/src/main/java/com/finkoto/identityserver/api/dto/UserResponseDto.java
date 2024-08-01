package com.finkoto.identityserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private String id;
    private String username;
    private String lastName;
    private String firstName;
    private boolean enabled;


}