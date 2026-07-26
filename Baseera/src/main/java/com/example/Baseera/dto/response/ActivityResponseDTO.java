package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Activity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ActivityResponseDTO {

    private Long id;
    private String name;
    private String description;
    private Integer minAge;
    private Integer maxAge;
    private String targetCondition;

    public static ActivityResponseDTO fromEntity(Activity entity) {

        ActivityResponseDTO dto = new ActivityResponseDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setMinAge(entity.getMinAge());
        dto.setMaxAge(entity.getMaxAge());
        dto.setTargetCondition(entity.getTargetCondition());

        return dto;
    }

    public static List<ActivityResponseDTO> fromEntity(List<Activity> entities) {

        List<ActivityResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Activity entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}