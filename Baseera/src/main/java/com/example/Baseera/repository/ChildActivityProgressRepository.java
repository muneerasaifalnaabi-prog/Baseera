package com.example.Baseera.repository;

import com.example.Baseera.entity.ChildActivityProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChildActivityProgressRepository extends JpaRepository<ChildActivityProgress, Long> {

    @Query("SELECT p FROM ChildActivityProgress p WHERE p.isActive = true AND p.child.id = :childId " +
            "ORDER BY p.createdAt DESC")
    List<ChildActivityProgress> findAllByChildId(@Param("childId") Long childId);

    @Query("SELECT p FROM ChildActivityProgress p WHERE p.isActive = true " +
            "AND p.child.id = :childId AND p.activity.id = :activityId")
    Optional<ChildActivityProgress> findByChildIdAndActivityId(
            @Param("childId") Long childId, @Param("activityId") Long activityId);
}
