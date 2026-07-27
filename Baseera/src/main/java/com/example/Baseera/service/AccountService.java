package com.example.Baseera.service;

import com.example.Baseera.dto.request.AccountRequestDTO;
import com.example.Baseera.dto.response.AccountResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.exception.DuplicateResourceException;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    //create account and check if the account already exist
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }
        Account account = request.toEntity();
        Account savedAccount = accountRepository.save(account);
        return AccountResponseDTO.fromEntity(savedAccount);
    }

    //Get All Accounts
    public List<AccountResponseDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findByIsActiveTrue();
        return AccountResponseDTO.fromEntity(accounts);
    }

    //Get Account By Id
    public AccountResponseDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        if (!account.getIsActive()) {
            throw new ResourceNotFoundException("Account not found.");
        }

        return AccountResponseDTO.fromEntity(account);
    }

    // Update an existing account
    public AccountResponseDTO updateAccount(Long id, AccountRequestDTO request) {

        // Find the account by its ID or throw an exception if it does not exist
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        // Ensure the account is active (not soft deleted)
        if (!account.getIsActive()) {
            throw new ResourceNotFoundException("Account not found.");
        }

        // Check if the new email is already used by another account
        if (!account.getEmail().equals(request.getEmail())
                && accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        // Update the account fields with the new values
        request.applyTo(account);

        // Save the updated account
        Account updatedAccount = accountRepository.save(account);

        // Convert the updated entity to a response DTO
        return AccountResponseDTO.fromEntity(updatedAccount);
    }

    // Soft delete an account
    public void deleteAccount(Long id) {

        // Find the account by its ID or throw an exception if it does not exist
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        // Ensure the account is active before deleting
        if (!account.getIsActive()) {
            throw new ResourceNotFoundException("Account not found.");
        }

        // Mark the account as inactive instead of removing it from the database
        account.setIsActive(false);

        // Save the updated account status
        accountRepository.save(account);
    }
}
