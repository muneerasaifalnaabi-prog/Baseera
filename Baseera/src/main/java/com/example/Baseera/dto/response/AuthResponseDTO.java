package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Account;
import com.example.Baseera.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String fullName;
    private String email;
    private Role role;

    public static AuthResponseDTO fromEntity(Account account, String token) {
        AuthResponseDTO dto = new AuthResponseDTO();

        dto.setToken(token);
        dto.setFullName(account.getFullName());
        dto.setEmail(account.getEmail());
        dto.setRole(account.getRole());

        return dto;
    }
}
