package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Account;
import com.example.Baseera.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Note: no role field here — role is ALWAYS hardcoded to Role.PARENT
 * inside toEntity(). Nothing the client sends can change it.
 */
@Data
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    /**
     * encodedPassword must already be BCrypt-hashed by the caller
     * (AccountService) before this is invoked — this DTO never persists
     * the raw password.
     */
    public Account toEntity(String encodedPassword) {
        Account account = new Account();

        account.setFullName(fullName);
        account.setEmail(email);
        account.setPassword(encodedPassword);
        account.setRole(Role.PARENT);

        return account;
    }
}
