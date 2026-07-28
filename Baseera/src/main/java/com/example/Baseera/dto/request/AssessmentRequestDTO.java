package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Assessment;
import com.example.Baseera.entity.Child;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Note: no riskLevel or suggestedCondition fields — those are never
 * client-supplied, they're filled in by Gemini after toEntity() is saved.
 */

@Data
@NoArgsConstructor
public class AssessmentRequestDTO {
        @NotBlank(message = "Description is required")
        @Size(min = 10, message = "Please provide a more detailed description")
        private String description;

        public Assessment toEntity(Child child) {
                Assessment assessment = new Assessment();

                assessment.setChild(child);
                assessment.setDescription(description);

                return assessment;
        }
}