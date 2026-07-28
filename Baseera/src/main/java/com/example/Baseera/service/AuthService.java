package com.example.Baseera.service;

import com.example.Baseera.dto.request.LoginRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AccountRepository;
import com.example.Baseera.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //****========
    // public: verify email + password via Spring Security's AuthenticationManager
    // (delegates to CustomUserDetailsService + the BCryptPasswordEncoder bean),
    // then issue a JWT carrying username + role
    //==========****
    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        Account account = accountRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        String token = jwtUtil.generateToken(account.getEmail(), account.getRole().name());

        return AuthResponseDTO.fromEntity(account, token);
    }
}

