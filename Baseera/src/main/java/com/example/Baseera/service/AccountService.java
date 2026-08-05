package com.example.Baseera.service;

import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.response.AccountResponseDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.entity.RefreshToken;
import com.example.Baseera.exception.DuplicateResourceException;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AccountRepository;
import com.example.Baseera.repository.RefreshTokenRepository;
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
    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthResponseDTO register(RegisterRequestDTO dto) {

        if (accountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException(
                    "An account with this email already exists"
            );
        }


        // Encode password
        String encodedPassword =
                passwordEncoder.encode(dto.getPassword());


        // Create account
        Account account =
                dto.toEntity(encodedPassword);


        // Save account
        Account saved =
                accountRepository.save(account);



        // Generate access token
        String accessToken =
                jwtUtil.generateToken(
                        saved.getEmail(),
                        saved.getRole().name()
                );



        // Generate refresh token and save it
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(saved);



        // Return response
        return AuthResponseDTO.fromEntity(
                saved,
                accessToken,
                refreshToken.getToken()
        );
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Account account = dto.toEntity(encodedPassword);
        Account saved = accountRepository.save(account);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());
        return AuthResponseDTO.fromEntity(saved, token);
    }

    // findAll(), not findByIsActiveTrue() — admin needs to see deactivated
    // accounts too, not just active ones.
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

    public long getActiveCount() {
        return accountRepository.countByIsActiveTrue();
    }

    public long getDeactivatedCount() {
        return accountRepository.countByIsActiveFalse();
    }

    // Raw rows, no DTO — each row is [date, count]. Jackson (Spring's
    // built-in JSON library) serializes Object[] as a plain JSON array
    // automatically, no extra class needed for something this small.
    public List<Object[]> getRegistrationTrend() {
        return accountRepository.findDailyRegistrationCountsRaw();
    }
}