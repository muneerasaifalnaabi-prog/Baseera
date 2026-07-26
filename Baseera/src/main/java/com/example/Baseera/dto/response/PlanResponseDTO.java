package com.example.Baseera.dto.response;


import com.example.Baseera.entity.Plan;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class PlanResponseDTO {

    private Long id;
    private String description;
    private String category;
    private String condition;
    private Integer minAgeMonths;
    private Integer maxAgeMonths;

    public static PlanResponseDTO fromEntity(Plan entity) {

        PlanResponseDTO dto = new PlanResponseDTO();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setCondition(entity.getCondition());
        dto.setMinAgeMonths(entity.getMinAgeMonths());
        dto.setMaxAgeMonths(entity.getMaxAgeMonths());

        return dto;
    }

    public static List<PlanResponseDTO> fromEntity(List<Plan> entities) {

        List<PlanResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Plan entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}
