package com.example.Baseera.security;
import com.example.Baseera.entity.Account;
import com.example.Baseera.repository.AccountRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + email));

        // Convert our user into the format Spring Security understands
        return org.springframework.security.core.userdetails.User
                .withUsername(account.getEmail())
                .password(account.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
                .build();
    }
}