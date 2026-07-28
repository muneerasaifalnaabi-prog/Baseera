package com.example.Baseera.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Turn our text secret into a proper cryptographic key
    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 1. CREATE a token for a given username and role
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // 2. READ the username out of a token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 3. READ the role out of a token
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // 4. CHECK whether a token is still valid
    public boolean isTokenValid(String token) {
        try {
            Date expiry = extractAllClaims(token).getExpiration();
            return expiry.after(new Date()); // not expired yet
        } catch (Exception e) {
            return false; // any error means invalid (bad signature, etc.)
        }
    }

    // Helper: open the token and read all its claims (data)
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}