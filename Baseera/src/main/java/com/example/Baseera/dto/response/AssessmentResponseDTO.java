package com.example.Baseera.dto.response;
import com.example.Baseera.entity.Assessment;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.enums.RiskLevel;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor

public class AssessmentResponseDTO {
    private Long id;
    private Long childId;
    private String description;
    private RiskLevel riskLevel;
    private ConditionType suggestedCondition;
    private LocalDateTime createdAt;

    public static AssessmentResponseDTO fromEntity(Assessment entity) {

        AssessmentResponseDTO dto = new AssessmentResponseDTO();

        dto.setId(entity.getId());
        dto.setChildId(entity.getChild().getId());
        dto.setDescription(entity.getDescription());
        dto.setRiskLevel(entity.getRiskLevel());
        dto.setSuggestedCondition(entity.getSuggestedCondition());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    public static List<AssessmentResponseDTO> fromEntity(List<Assessment> entities) {

        List<AssessmentResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Assessment entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }
}
