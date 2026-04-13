package com.ordernest.controller;

import com.ordernest.dto.AuthResponseDTO;
import com.ordernest.dto.LoginRequestDTO;
import com.ordernest.dto.RegisterRequestDTO;
import com.ordernest.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;

    public  AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        AuthResponseDTO authResponseDTO = userService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO authResponseDTO = userService.loginUser(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
    }
}