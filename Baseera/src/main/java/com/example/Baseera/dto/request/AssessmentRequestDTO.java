package com.example.Baseera.dto.request;
import com.example.Baseera.entity.Assessment;
import com.example.Baseera.entity.Child;
import lombok.Data;

@Data
public class AssessmentRequestDTO {

    private Long childId;

    private String parentDescription;

    private String riskLevel;

    private String suggestedCondition;

    private String aiNarrative;

    public Assessment toEntity(Child child) {

        return Assessment.builder()
                .child(child)
                .parentDescription(parentDescription)
                .riskLevel(riskLevel)
                .suggestedCondition(suggestedCondition)
                .aiNarrative(aiNarrative)
                .build();
    }
}
