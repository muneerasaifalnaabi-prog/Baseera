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

    long countByIsActiveTrue();
    long countByIsActiveFalse();

    // Last 7 days of registrations, grouped by real calendar date —
    // genuine data pulled from createdAt, not simulated. DATE(created_at)
    // strips the time portion so multiple sign-ups on the same day
    // count together into one row.
    @Query(value = "SELECT DATE(created_at) as date, COUNT(*) as count " +
            "FROM account WHERE created_at >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE(created_at) ORDER BY date ASC", nativeQuery = true)
    List<Object[]> findDailyRegistrationCountsRaw();

}