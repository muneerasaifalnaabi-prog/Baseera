package com.example.Baseera.controller;

import com.example.Baseera.dto.request.LoginRequestDTO;
import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.service.AccountService;
import com.example.Baseera.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    // public: register a new parent account
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.register(dto));
    }

    // public: log in, receive a JWT
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}

