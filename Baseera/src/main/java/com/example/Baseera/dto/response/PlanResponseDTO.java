package com.example.Baseera.dto.response;


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

    public static PlanResponseDTO fromEntity(Plans entity) {

        PlanResponseDTO dto = new PlanResponseDTO();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCategory(entity.getCategory());
        dto.setCondition(entity.getConditions());
        dto.setMinAgeMonths(entity.getMinAgeMonths());
        dto.setMaxAgeMonths(entity.getMaxAgeMonths());

        return dto;
    }

    public static List<PlanResponseDTO> fromEntity(List<Plans> entities) {

        List<PlanResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Plans entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}
