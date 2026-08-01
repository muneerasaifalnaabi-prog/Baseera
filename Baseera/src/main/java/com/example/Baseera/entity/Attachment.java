package com.example.Baseera.entity;


import com.example.Baseera.enums.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A file uploaded by the parent for a child — typically the specialist's
 * written report received after visiting a recommended Center.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Attachment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private Child child;

    @Column(nullable = false)
    private String originalFileName;

    /**
     * Storage location (disk path, S3 key, etc.) — never exposed directly
     * to the client; only fileName/id are returned in responses.
     */
    @Column(nullable = false)
    private String storedFilePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;
}
