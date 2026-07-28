package com.example.Baseera.dto.response;
import com.example.Baseera.enums.DocumentType;

import java.time.LocalDateTime;


/**
 * Note: no filePath field — the internal storage location is never
 * exposed to the client.
 */
public record AttachmentResponseDTO (
        Long id,
        Long childId,
        String fileName,
        DocumentType documentType,
        LocalDateTime createdAt
) {
}