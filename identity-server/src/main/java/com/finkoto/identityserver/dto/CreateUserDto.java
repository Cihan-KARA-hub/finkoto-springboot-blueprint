package com.finkoto.identityserver.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {

    private String username;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private String email;
}
