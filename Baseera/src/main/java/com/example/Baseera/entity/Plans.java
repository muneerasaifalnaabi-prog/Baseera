package com.example.Baseera.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Plans extends BaseEntity {

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String conditions; // "ASD" or "ADHD"

    @Column(nullable = false)
    private Integer minAgeMonths;

    @Column(nullable = false)
    private Integer maxAgeMonths;

    @OneToMany(mappedBy = "plans")
    private List<Child> children = new ArrayList<>();
}