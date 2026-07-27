package com.example.Baseera.entity;
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

    @Lob
    private String parentDescription;

    private String riskLevel;

    private String suggestedCondition;

    @Lob
    private String aiNarrative;

}
