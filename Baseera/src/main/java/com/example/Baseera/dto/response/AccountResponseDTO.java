package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Account;
import com.example.Baseera.enums.Roles;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
public class AccountResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private Roles role;

    public static AccountResponseDTO fromEntity(Account entity) {

        AccountResponseDTO dto = new AccountResponseDTO();

        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        dto.setRole(entity.getRole());

        return dto;
    }

    public static List<AccountResponseDTO> fromEntity(List<Account> entities) {

        List<AccountResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Account entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}