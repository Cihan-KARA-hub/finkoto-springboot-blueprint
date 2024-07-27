package com.finkoto.identityserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTokenDto {
    private String userName;
    private String password;

}
