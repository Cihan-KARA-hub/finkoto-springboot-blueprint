package com.finkoto.identityserver.controller;

import com.finkoto.identityserver.dto.*;
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
    //Admin Token
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> createToken(@RequestBody CreateTokenDto dto) {
        TokenResponseDto tokenResponse = service.getToken(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok(tokenResponse);
    }
    //User token
    @PostMapping("/user-token")
    public TokenResponseDto  createUserToken(@RequestBody CreateTokenDto dto) {
        TokenResponseDto  tokenResponse = service.getUserToken(dto.getUserName(), dto.getPassword());
        return ResponseEntity.ok(tokenResponse).getBody();}
    //all user
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return  ResponseEntity.ok(service.getAllUsers());
    }
    //get user id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getByIdUser(@PathVariable String id) {
      UserResponseDto user = service.getByIdUser(id);
        return ResponseEntity.ok(user);
    }
    //create user
    @PostMapping()
    public  ResponseEntity<String> createUser(@RequestBody CreateUserDto createUserDto){

        try {
            service.createUser(createUserDto);

            return ResponseEntity.ok(createUserDto.getUsername() + " created");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user");
        }}
    //Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        service.userDelete(id);
        return ResponseEntity.ok("User deleted");
    }
    //Update user
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateDto userUpdateDto, @PathVariable String id) {
        service.updateUser(userUpdateDto, id);
        return ResponseEntity.ok(userUpdateDto.getUsername());
    }
    //Reset password
    @PutMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody UserPasswordDto passwordDto,@PathVariable String id){
        service.resetPassword(passwordDto,id);
        return ResponseEntity.ok(passwordDto.getValue());
    }


}