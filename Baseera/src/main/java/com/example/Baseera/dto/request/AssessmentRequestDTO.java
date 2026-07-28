package com.example.Baseera.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Note: no riskLevel or suggestedCondition fields — those are never
 * client-supplied. They are computed by Gemini in AssessmentService
 * after this description is submitted.
 */
public record AssessmentRequestDTO(

        @NotBlank(message = "Description is required")
        @Size(min = 10, message = "Please provide a more detailed description")
        String description
) {
}