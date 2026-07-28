package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.Child;
import com.example.Baseera.enums.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metadata bound alongside the multipart file. The MultipartFile itself
 * is a separate controller parameter — it never goes through this DTO.
 */
@Data
@NoArgsConstructor
public class AttachmentRequestDTO {

        @NotNull(message = "Document type is required")
        private DocumentType documentType;

        /**
         * originalFileName and storedFilePath aren't on this DTO because they
         * only exist once FileStorageService has actually written the bytes —
         * they're passed in directly here, not bound from client JSON.
         */
        public Attachment toEntity(Child child, String originalFileName, String storedFilePath) {
                Attachment attachment = new Attachment();

                attachment.setChild(child);
                attachment.setOriginalFileName(originalFileName);
                attachment.setStoredFilePath(storedFilePath);
                attachment.setDocumentType(documentType);

                return attachment;
        }
}
