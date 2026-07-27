package com.example.Baseera.dto.request;
import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.AttachmentAnalysis;
import lombok.Data;

@Data
public class AttachmentAnalysisRequestDTO {

    private Long attachmentId;

    private String improvementSigns;

    private String progressSummary;

    private String suggestedGoalTags;

    public AttachmentAnalysis toEntity(Attachment attachment) {

        return AttachmentAnalysis.builder()
                .attachment(attachment)
                .improvementSigns(improvementSigns)
                .progressSummary(progressSummary)
                .suggestedGoalTags(suggestedGoalTags)
                .build();
    }
}
