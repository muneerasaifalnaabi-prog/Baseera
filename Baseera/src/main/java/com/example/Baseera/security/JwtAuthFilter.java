package com.example.Baseera.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {

        // 1. Look for the Authorization header
        String authHeader = request.getHeader("Authorization");

        // 2. If it's missing or doesn't start with "Bearer ", skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cut off the word "Bearer " to get the raw token
        String token = authHeader.substring(7);

        // 4. If the token is valid, tell Spring Security who this user is
        if (jwtUtil.isTokenValid(token)) {
            String username = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            var authority = new SimpleGrantedAuthority("ROLE_" + role);

            var authToken = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(authority));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 5. Always continue to the next filter
        filterChain.doFilter(request, response);
    }
}