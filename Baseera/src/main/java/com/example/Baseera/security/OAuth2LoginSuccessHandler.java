package com.example.Baseera.security;

import com.example.Baseera.entity.Account;
import com.example.Baseera.enums.Role;
import com.example.Baseera.repository.AccountRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;

    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    public OAuth2LoginSuccessHandler(AccountRepository accountRepository, JwtUtil jwtUtil) {
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        String fullName = oauthUser.getAttribute("name");

        Account account = accountRepository.findByEmail(email)
                .orElseGet(() -> {
                    Account newAccount = new Account();
                    newAccount.setEmail(email);
                    newAccount.setFullName(fullName != null ? fullName : email);
                    newAccount.setPassword(null);
                    newAccount.setRole(Role.PARENT);
                    return accountRepository.save(newAccount);
                });

        String token = jwtUtil.generateToken(account.getEmail(), account.getRole().name());

        response.sendRedirect(redirectUri + "?token=" + token);
    }
}
