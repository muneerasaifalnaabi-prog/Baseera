package com.example.Baseera.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attachment_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentAnalysis extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attachment_id", nullable = false)
    private Attachment attachment;

    @Lob
    @Column(nullable = false)
    private String improvementSigns;

    @Lob
    @Column(nullable = false)
    private String progressSummary;

    /**
     * Matched against Activity.targetCondition in the service layer to
     * decide which activities get surfaced next for this child.
     */
    @ElementCollection
    @CollectionTable(
            name = "attachment_analysis_goal_tags",
            joinColumns = @JoinColumn(name = "attachment_analysis_id")
    )
    @Column(name = "goal_tag", nullable = false)
    @Builder.Default
    private List<String> suggestedGoalTags = new ArrayList<>();

}
