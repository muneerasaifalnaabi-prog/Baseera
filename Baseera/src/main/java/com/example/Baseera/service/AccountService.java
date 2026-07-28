package com.example.Baseera.service;

import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.exception.DuplicateResourceException;
import com.example.Baseera.repository.AccountRepository;
import com.example.Baseera.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // public: register a new parent account. role is hardcoded to PARENT
    // inside RegisterRequestDTO.toEntity() — never accepted from the client.

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (accountRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }
        // Raw password touches memory once, right here, to compute the hash.
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        Account account = dto.toEntity(encodedPassword);
        Account saved = accountRepository.save(account);
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole().name());
        return AuthResponseDTO.fromEntity(saved, token);
    }
}
