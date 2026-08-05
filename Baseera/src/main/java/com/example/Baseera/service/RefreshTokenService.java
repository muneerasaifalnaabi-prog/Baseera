package com.example.Baseera.service;


import com.example.Baseera.entity.Account;
import com.example.Baseera.entity.RefreshToken;
import com.example.Baseera.repository.RefreshTokenRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service
public class RefreshTokenService {

    ;
    private final RefreshTokenRepository refreshTokenRepository;


    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }


    // Create refresh token
    public RefreshToken createRefreshToken(
            Account account
    ) {


        RefreshToken refreshToken = new RefreshToken();


        refreshToken.setAccount(account);


        refreshToken.setToken(
                UUID.randomUUID().toString()
        );


        // 7 days expiration
        refreshToken.setExpiryDate(
                new Date(
                        System.currentTimeMillis()
                                + (7L * 24 * 60 * 60 * 1000)
                )
        );


        return refreshTokenRepository.save(refreshToken);
    }


    // Find refresh token
    public RefreshToken findByToken(
            String token
    ) {


        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Refresh token not found"
                        )
                );
    }
     @Transactional
    public void deleteByToken(String token) {

        refreshTokenRepository.deleteByToken(token);

    }


    // Check expiration
    public RefreshToken verifyExpiration(
            RefreshToken refreshToken
    ) {


        if (refreshToken.getExpiryDate()
                .before(new Date())) {


            refreshTokenRepository.delete(
                    refreshToken
            );


            throw new RuntimeException(
                    "Refresh token expired"
            );
        }


        return refreshToken;
    }



}