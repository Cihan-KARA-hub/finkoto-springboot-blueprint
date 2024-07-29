package com.finkoto.identityserver.controller;

import com.finkoto.identityserver.dto.*;
import com.finkoto.identityserver.services.UserService;
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
    public ResponseEntity<TokenResponseDto> createToken(@RequestBody CreateTokenDto dto) {
        TokenResponseDto tokenResponse = service.getToken(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/user-token")
    public TokenResponseDto  createUserToken(@RequestBody CreateTokenDto dto) {
        TokenResponseDto  tokenResponse = service.getUserToken(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok(tokenResponse).getBody();}


    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return  ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getByIdUser(@PathVariable String id) {
      UserResponseDto user = service.getByIdUser(id);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/users")
    public  CreateUserDto createUser(@RequestBody CreateUserDto createUserDto){
        //burdan devam et
        CreateUserDto createUserDto1
    }

}