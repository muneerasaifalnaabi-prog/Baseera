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

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken createRefreshToken(Account account) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByAccount(account)
                .orElse(new RefreshToken());

        refreshToken.setAccount(account);

        refreshToken.setToken(
                UUID.randomUUID().toString()
        );

        refreshToken.setExpiryDate(
                new Date(
                        System.currentTimeMillis()
                                + (7L * 24 * 60 * 60 * 1000)
                )
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found")
                );
    }

    @Transactional
    public void deleteByToken(String token) {

        refreshTokenRepository.deleteByToken(token);
    }

    public RefreshToken verifyExpiration(
            RefreshToken refreshToken
    ) {

        if (refreshToken.getExpiryDate().before(new Date())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        return refreshToken;
    }
}