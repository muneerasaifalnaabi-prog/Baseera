package com.example.Baseera.service;

import com.example.Baseera.dto.request.LoginRequestDTO;
import com.example.Baseera.dto.request.RefreshTokenRequestDTO;
import com.example.Baseera.dto.response.AuthResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.entity.RefreshToken;
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


    @Autowired
    private RefreshTokenService refreshTokenService;



    // LOGIN
    public AuthResponseDTO login(LoginRequestDTO dto) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(),
                            dto.getPassword()
                    )
            );

        } catch (BadCredentialsException e) {

            throw new BadCredentialsException(
                    "Invalid email or password"
            );
        }


        Account account =
                accountRepository.findByEmail(dto.getEmail())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found"
                                )
                        );


        // Generate JWT access token
        String accessToken =
                jwtUtil.generateToken(
                        account.getEmail(),
                        account.getRole().name()
                );


        // Generate refresh token and save it
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(account);



        return AuthResponseDTO.fromEntity(
                account,
                accessToken,
                refreshToken.getToken()
        );
    }





    // REFRESH TOKEN
    public AuthResponseDTO refreshToken(
            RefreshTokenRequestDTO request
    ) {


        // Find refresh token in database
        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.getRefreshToken()
                );


        // Check expiration
        refreshTokenService.verifyExpiration(
                refreshToken
        );


        // Get account from refresh token
        Account account =
                refreshToken.getAccount();



        // Create new access token
        String newAccessToken =
                jwtUtil.generateToken(
                        account.getEmail(),
                        account.getRole().name()
                );



        return AuthResponseDTO.fromEntity(
                account,
                newAccessToken,
                refreshToken.getToken()
        );

    }
    public void logout(RefreshTokenRequestDTO request) {

        refreshTokenService.deleteByToken(
                request.getRefreshToken()
        );

    }

}