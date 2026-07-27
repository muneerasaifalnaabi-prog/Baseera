package com.example.Baseera.entity;
import jakarta.persistence.*;
import lombok.*;

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
    private String improvementSigns;

    @Lob
    private String progressSummary;

    private String suggestedGoalTags;

}
