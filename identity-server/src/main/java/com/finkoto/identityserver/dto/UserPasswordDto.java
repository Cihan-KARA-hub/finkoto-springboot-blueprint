package com.finkoto.identityserver.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPasswordDto {
    private String type;
    private String value;
    private boolean temporary;
}
