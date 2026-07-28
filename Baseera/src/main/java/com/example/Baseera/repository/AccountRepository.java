package com.example.Baseera.repository;

import com.example.Baseera.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.isActive = true AND a.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

    List<Account> findByIsActiveTrue();
}