package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Account;
import com.example.Baseera.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AccountRequestDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private Role role;

    public Account toEntity() {

        Account account = new Account();

        account.setEmail(email);
        account.setPassword(password);
        account.setFullName(fullName);
        account.setRole(role);

        return account;
    }

    public void applyTo(Account account) {

        account.setEmail(email);
        account.setPassword(password);
        account.setFullName(fullName);
        account.setRole(role);
    }
}
