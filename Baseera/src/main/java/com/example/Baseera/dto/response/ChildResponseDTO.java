package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Child;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChildResponseDTO {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private Long parentId;
    private Long planId;

    public static ChildResponseDTO fromEntity(Child entity) {

        ChildResponseDTO dto = new ChildResponseDTO();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());

        if (entity.getParent() != null) {
            dto.setParentId(entity.getParent().getId());
        }

        if (entity.getPlans() != null) {
            dto.setPlanId(entity.getPlans().getId());
        }

        return dto;
    }

    public static List<ChildResponseDTO> fromEntity(List<Child> entities) {

        List<ChildResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Child entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}
