package com.example.Baseera.service;

import com.example.Baseera.dto.request.RegisterRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.entity.RefreshToken;
import com.example.Baseera.exception.DuplicateResourceException;
import com.example.Baseera.repository.AccountRepository;
import com.example.Baseera.repository.RefreshTokenRepository;
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
    @Autowired
    private RefreshTokenService refreshTokenService;

    // public: register a new parent account. role is hardcoded to PARENT
    // inside RegisterRequestDTO.toEntity() — never accepted from the client.

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
    }
}
