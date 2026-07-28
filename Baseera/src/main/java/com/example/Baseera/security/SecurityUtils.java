package com.example.Baseera.security;

import com.example.Baseera.entity.Account;
import com.example.Baseera.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final AccountRepository accountRepository;

    public Account getCurrentAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user in context");
        }
        String email = auth.getName();
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated account not found: " + email));
    }

    public Long getCurrentAccountId() {
        return getCurrentAccount().getId();
    }
}

