package com.finkoto.identityserver.controller;

import com.finkoto.identityserver.dto.CreateTokenDto;
import com.finkoto.identityserver.dto.UserResponseDto;
import com.finkoto.identityserver.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/token")
    public ResponseEntity<String> createToken(@RequestBody CreateTokenDto dto) {
        String tokenResponse = service.getToken(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@RequestBody CreateTokenDto dto) {
        try {
            List<UserResponseDto> users = service.getAllUser(dto.getUserName(), dto.getPassword());
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/authenticated")
    public String authenticated() {
        return "authenticated";
    }
}
