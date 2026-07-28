package com.example.Baseera.repository;

import com.example.Baseera.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    @Query("SELECT a FROM Attachment a WHERE a.isActive = true AND a.id = :id")
    Optional<Attachment> findAttachmentById(@Param("id") Long id);

    @Query("SELECT a FROM Attachment a WHERE a.isActive = true AND a.child.id = :childId ORDER BY a.createdAt DESC")
    List<Attachment> findAllByChildId(@Param("childId") Long childId);
}

