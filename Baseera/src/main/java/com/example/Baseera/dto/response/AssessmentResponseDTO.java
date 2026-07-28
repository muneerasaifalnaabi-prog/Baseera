package com.example.Baseera.dto.response;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.enums.RiskLevel;


import java.time.LocalDateTime;


public record AssessmentResponseDTO (
        Long id,
        Long childId,
        String description,
        RiskLevel riskLevel,
        ConditionType suggestedCondition,
        LocalDateTime createdAt
) {
}
