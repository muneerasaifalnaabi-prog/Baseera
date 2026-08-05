package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Child;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChildAdminResponseDTO {

    private Long id;
    private String fullName;
    private Integer age;
    private String gender;

    private Long parentId;
    private String parentFullName;
    private String parentEmail;

    public static ChildAdminResponseDTO fromEntity(Child entity) {
        ChildAdminResponseDTO dto = new ChildAdminResponseDTO();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setAge(Period.between(entity.getDateOfBirth(), LocalDate.now()).getYears());
        dto.setGender(entity.getGender());

        dto.setParentId(entity.getParent().getId());
        dto.setParentFullName(entity.getParent().getFullName());
        dto.setParentEmail(entity.getParent().getEmail());

        return dto;
    }

    public static List<ChildAdminResponseDTO> fromEntity(List<Child> entities) {
        List<ChildAdminResponseDTO> dtos = new ArrayList<>();
        for (Child entity : entities) {
            dtos.add(fromEntity(entity));
        }
        return dtos;
    }
}