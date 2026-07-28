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
public class ChildResponseDTO {

    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private Integer age;
    private String gender;

    /** age is computed server-side from dateOfBirth — never stored, never client-supplied. */
    public static ChildResponseDTO fromEntity(Child entity) {

        ChildResponseDTO dto = new ChildResponseDTO();

        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setAge(Period.between(entity.getDateOfBirth(), LocalDate.now()).getYears());
        dto.setGender(entity.getGender());

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
