package com.example.Baseera.dto.response;


import java.time.LocalDateTime;
import java.util.List;

/**
 * This IS the "current plan" the parent sees at
 * GET /api/children/{childId}/current-plan — the most recent
 * AttachmentAnalysis across all of the child's uploaded reports.
 */
public record AttachmentAnalysisResponseDTO (
        Long id,
        Long attachmentId,
        String improvementSigns,
        String progressSummary,
        List<String> suggestedGoalTags,
        LocalDateTime createdAt
) {
}
