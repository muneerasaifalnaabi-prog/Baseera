package com.example.Baseera.dto.response;
import com.example.Baseera.entity.Assessment;
import com.example.Baseera.enums.RiskLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssessmentResponseDTO {
    private Long id;

    private Long childId;

    private String parentDescription;

    private RiskLevel riskLevel;

    private String suggestedCondition;

    private String aiNarrative;

    private Boolean isActive;

    public static AssessmentResponseDTO fromEntity(Assessment assessment) {

        return AssessmentResponseDTO.builder()
                .id(assessment.getId())
                .childId(assessment.getChild().getId())
                .parentDescription(assessment.getParentDescription())
                .riskLevel(assessment.getRiskLevel())
                .suggestedCondition(assessment.getSuggestedCondition())
                .aiNarrative(assessment.getAiNarrative())
                .isActive(assessment.getIsActive())
                .build();
    }
}
