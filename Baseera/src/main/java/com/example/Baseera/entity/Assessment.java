package com.example.Baseera.entity;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "assessment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment extends BaseEntity{
    @OneToOne(fetch = FetchType.LAZY)
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
