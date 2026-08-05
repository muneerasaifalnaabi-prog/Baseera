package com.example.Baseera.repository;

import com.example.Baseera.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;


public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken> findByToken(String token);


    @Modifying
    @Transactional
    void deleteByToken(String token);
}