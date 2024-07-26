package com.finkoto.identityserver.controller;



import com.finkoto.identityserver.dto.CreateTokenDto;
import com.finkoto.identityserver.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
@RestController
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
    @GetMapping("/authenticated")
    public String authenticated() {

        return "authenticated";
    }

}
