package com.example.Baseera.entity;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;
/**
 * A single behavior assessment for a child. A child can have MANY
 * assessments over time (reassessment) — each one is timestamped via
 * BaseEntity.createdAt. The most recent one drives condition-based
 * matching (centers, activities).
 */

@Entity
@Table(name = "assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    /**
     * Free-text description of behaviors submitted by the parent,
     * sent to Gemini for structured analysis.
     */
    @Lob
    @Column(nullable = false)
    private String description;

    /**
     * Returned by Gemini. Null until the AI call completes successfully.
     */
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    /**
     * Returned by Gemini. Drives center and activity recommendation.
     */
    @Enumerated(EnumType.STRING)
    private ConditionType suggestedCondition;

}
