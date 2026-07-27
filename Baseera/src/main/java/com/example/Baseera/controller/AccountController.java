package com.example.Baseera.controller;

import com.example.Baseera.dto.request.AccountRequestDTO;
import com.example.Baseera.dto.response.AccountResponseDTO;
import com.example.Baseera.service.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    // Create a new account
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDTO createAccount(@Valid @RequestBody AccountRequestDTO request) {
        return accountService.createAccount(request);
    }
    // Retrieve all active accounts
    @GetMapping
    public List<AccountResponseDTO> getAllAccounts() {
        return accountService.getAllAccounts();
    }
    // Retrieve an account by its ID
    @GetMapping("/{id}")
    public AccountResponseDTO getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }
    // Update an existing account
    @PutMapping("/{id}")
    public AccountResponseDTO updateAccount(@PathVariable Long id, @Valid @RequestBody AccountRequestDTO request) {
        return accountService.updateAccount(id, request);
    }
    // Soft delete an account
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

}
