package com.example.Baseera.service;

import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.response.AccountResponseDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.dto.response.DailyRegistrationDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.exception.DuplicateResourceException;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AccountRepository;
import com.example.Baseera.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (accountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Account account = dto.toEntity(encodedPassword);
        Account saved = accountRepository.save(account);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());
        return AuthResponseDTO.fromEntity(saved, token);
    }

    //****========
    // admin: EVERY account, active and deactivated alike — findAll(),
    // not the isActive-filtered query used elsewhere in the app. Admin
    // needs to see the whole picture, including who's been suspended.
    //==========****
    public List<AccountResponseDTO> getAllAccounts() {
        return AccountResponseDTO.fromEntity(accountRepository.findAll());
    }

    public String deactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
        account.setIsActive(false);
        accountRepository.save(account);
        return "DEACTIVATED";
    }

    public String reactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
        account.setIsActive(true);
        accountRepository.save(account);
        return "REACTIVATED";
    }

    //****========
    // admin: dashboard stats — real counts and a real trend, nothing
    // fabricated. No login-activity tracking exists in this schema, so
    // "active throughout the day" isn't something this can honestly show;
    // registrations per day IS real data, since createdAt exists on every account.
    //==========****
    public long getActiveCount() {
        return accountRepository.countByIsActiveTrue();
    }

    public long getDeactivatedCount() {
        return accountRepository.countByIsActiveFalse();
    }

    public List<DailyRegistrationDTO> getRegistrationTrend() {
        return accountRepository.findDailyRegistrationCounts();
    }
}