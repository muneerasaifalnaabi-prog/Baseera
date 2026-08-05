package com.example.Baseera.controller;

import com.example.Baseera.dto.request.LoginRequestDTO;
import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.request.RefreshTokenRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.repository.RefreshTokenRepository;
import com.example.Baseera.service.AccountService;
import com.example.Baseera.service.AuthService;

import com.example.Baseera.service.RefreshTokenService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AccountService accountService;
    private final AuthService authService;


    public AuthController(
            AccountService accountService,
            AuthService authService
    ) {
        this.accountService = accountService;
        this.authService = authService;
    }



    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.register(dto));
    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto
    ) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> refreshToken(
            @RequestBody RefreshTokenRequestDTO request
    ) {

        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody RefreshTokenRequestDTO request
    ) {

        authService.logout(request);

        return ResponseEntity.ok(
                "Logout successful"
        );
    }

}