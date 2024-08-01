package com.finkoto.identityserver.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateDto {
    private String username;
    private String email;
    private String lastName;
    private String firstName;
    private boolean enabled;
}
