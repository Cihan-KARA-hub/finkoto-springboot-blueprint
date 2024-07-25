package com.finkoto.identityserver.controller;



import com.finkoto.identityserver.dto.CreateTokenDto;
import com.finkoto.identityserver.dto.TokenResponseDto;
import com.finkoto.identityserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> createToken(@RequestBody CreateTokenDto dto) {
        TokenResponseDto tokenResponse = service.createToken(dto);
        return ResponseEntity.ok(tokenResponse);
    }
    @GetMapping("/authenticated")

    public String authenticated() {
        return "authenticated";
    }

}
