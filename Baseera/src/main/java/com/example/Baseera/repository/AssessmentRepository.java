package com.example.Baseera.repository;

import com.example.Baseera.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    @Query("SELECT a FROM Assessment a WHERE a.isActive = true AND a.child.id = :childId ORDER BY a.createdAt DESC")
    List<Assessment> findAllByChildIdOrderByCreatedAtDesc(@Param("childId") Long childId);

    // Drives condition-based matching (centers, activities) — the most recent assessment wins.
    @Query("SELECT a FROM Assessment a WHERE a.isActive = true AND a.child.id = :childId " +
            "ORDER BY a.createdAt DESC LIMIT 1")
    Optional<Assessment> findLatestByChildId(@Param("childId") Long childId);
}
