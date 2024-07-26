package com.finkoto.identityserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTokenDto {
    private String userName;
    private String password;

}
