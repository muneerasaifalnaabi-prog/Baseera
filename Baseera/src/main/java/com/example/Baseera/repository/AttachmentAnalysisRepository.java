package com.example.Baseera.repository;

import com.example.Baseera.entity.AttachmentAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttachmentAnalysisRepository extends JpaRepository<AttachmentAnalysis,Long> {
    @Query("SELECT aa FROM AttachmentAnalysis aa WHERE aa.isActive = true AND aa.attachment.id = :attachmentId")
    Optional<AttachmentAnalysis> findByAttachmentId(@Param("attachmentId") Long attachmentId);

    // This IS "current plan" — the newest analysis across ALL of a child's uploaded reports.
    @Query("SELECT aa FROM AttachmentAnalysis aa WHERE aa.isActive = true AND aa.attachment.child.id = :childId " +
            "ORDER BY aa.createdAt DESC LIMIT 1")
    Optional<AttachmentAnalysis> findCurrentPlanForChild(@Param("childId") Long childId);

}
