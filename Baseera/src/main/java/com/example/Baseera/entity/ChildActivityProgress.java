package com.example.Baseera.entity;

import com.example.Baseera.enums.ProgressStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * The missing link between "here's the activity catalog" and "my child is
 * actually doing this one." Activity and Center stay standalone catalogs
 * on purpose (matched dynamically by age/condition), but THIS table is
 * the deliberate exception — a parent explicitly adds one specific
 * activity to one specific child, and tracks real progress on it.
 */
@Entity
@Table(name = "child_activity_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildActivityProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status;

    @Lob
    private String notes;
}
