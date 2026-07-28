package com.example.Baseera.controller;

import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Register a new parent account
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO register(@Valid @RequestBody RegisterRequestDTO request) {
        return accountService.register(request);
    }
}