package com.example.Baseera.dto.response;


import com.example.Baseera.entity.AttachmentAnalysis;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This IS the "current plan" returned by GET /api/children/{childId}/current-plan —
 * the most recent AttachmentAnalysis across all of the child's uploaded reports.
 */
@Data
@NoArgsConstructor
public class AttachmentAnalysisResponseDTO {
    private Long id;
    private Long attachmentId;
    private String improvementSigns;
    private String progressSummary;
    private List<String> suggestedGoalTags;
    private LocalDateTime createdAt;

    public static AttachmentAnalysisResponseDTO fromEntity(AttachmentAnalysis entity) {

        AttachmentAnalysisResponseDTO dto = new AttachmentAnalysisResponseDTO();

        dto.setId(entity.getId());
        dto.setAttachmentId(entity.getAttachment().getId());
        dto.setImprovementSigns(entity.getImprovementSigns());
        dto.setProgressSummary(entity.getProgressSummary());
        dto.setSuggestedGoalTags(entity.getSuggestedGoalTags());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

}
