package com.example.Baseera.controller;

import com.example.Baseera.dto.response.AccountResponseDTO;
import com.example.Baseera.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PutMapping("/{accountId}/deactivate")
    public ResponseEntity<String> deactivateAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.deactivateAccount(accountId));
    }

    @PutMapping("/{accountId}/reactivate")
    public ResponseEntity<String> reactivateAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.reactivateAccount(accountId));
    }
}