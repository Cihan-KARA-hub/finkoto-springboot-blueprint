package com.finkoto.identityserver.controller;

import com.finkoto.identityserver.dto.CreateTokenDto;
import com.finkoto.identityserver.dto.CreateUserDto;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable String id, @RequestParam String username, @RequestParam String password) {
        try {
            UserResponseDto user = service.getUserById(id, username, password);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserResponseDto userDto, @RequestParam String admin_username, @RequestParam String admin_password) {
        try {
            UserResponseDto createdUser = service.createUser(userDto, admin_username, admin_password);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id, @RequestParam String username, @RequestParam String password) {
        try {
            service.deleteUserById(id, username, password);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserById(@PathVariable String id, @RequestBody UserResponseDto updateUserDto, @RequestParam String username, @RequestParam String password) {
        try {
            service.updateUserById(id, updateUserDto, username, password);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}