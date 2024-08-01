package com.finkoto.identityserver.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUserDto {
    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
}
