package com.finkoto.identityserver.api.controller;


import com.finkoto.identityserver.api.dto.*;
import com.finkoto.identityserver.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
@Tag(name = "Admin Controller", description = "Admin API")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "password")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //all user
    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('app:read')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    //create user
    @PostMapping("/users")
    @PreAuthorize("hasAnyAuthority('app:create')")
    public ResponseEntity<String> createUser(@RequestBody CreateUserDto createUserDto) {

        try {
            service.createUser(createUserDto);

            return ResponseEntity.ok(createUserDto.getUsername() + " created");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }
    }

    //Delete user
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('app:delete')")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        service.userDelete(id);
        return ResponseEntity.ok("User deleted");
    }

    //Update user
    @PutMapping("/users/{id}")
    @PreAuthorize("hasAnyAuthority('app:update')")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable String id) {
        service.updateUser(userUpdateDto, id);
        return ResponseEntity.ok(userUpdateDto.getUsername());
    }

    //Reset password
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAnyAuthority('app:update')")
    public ResponseEntity<String> resetPassword(@RequestBody UserPasswordDto passwordDto, @PathVariable String id) {
        service.resetPassword(passwordDto, id);
        return ResponseEntity.ok(passwordDto.getValue());
    }
    //User token
    @PreAuthorize("hasAnyAuthority('app:read')")
    @PostMapping("/user-token")
    public TokenResponseDto createUserToken(@RequestBody CreateTokenDto dto) {
        TokenResponseDto tokenResponse = service.getUserToken(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok(tokenResponse).getBody();
    }
    //get user id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('app:read')")
    public ResponseEntity<UserResponseDto> getByIdUser(@PathVariable String id) {
        UserResponseDto user = service.getByIdUser(id);
        return ResponseEntity.ok(user);
    }

}