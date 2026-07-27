package com.example.Baseera.dto.response;
import com.example.Baseera.entity.AttachmentAnalysis;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentAnalysisResponseDTO {
    private Long id;

    private Long attachmentId;

    private String improvementSigns;

    private String progressSummary;

    private String suggestedGoalTags;

    private Boolean isActive;

    public static AttachmentAnalysisResponseDTO fromEntity(AttachmentAnalysis analysis) {

        return AttachmentAnalysisResponseDTO.builder()
                .id(analysis.getId())
                .attachmentId(analysis.getAttachment().getId())
                .improvementSigns(analysis.getImprovementSigns())
                .progressSummary(analysis.getProgressSummary())
                .suggestedGoalTags(analysis.getSuggestedGoalTags())
                .isActive(analysis.getIsActive())
                .build();
    }
}
