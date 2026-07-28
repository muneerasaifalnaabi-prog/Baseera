package com.example.Baseera.dto.request;

import com.example.Baseera.enums.DocumentType;
import jakarta.validation.constraints.NotNull;

/**
 * Metadata that travels alongside the uploaded file in the multipart
 * request. The MultipartFile itself is bound as a separate controller
 * parameter, not through this DTO.
 */
public record AttachmentRequestDTO (

        @NotNull(message = "Document type is required")
        DocumentType documentType
) {
}
