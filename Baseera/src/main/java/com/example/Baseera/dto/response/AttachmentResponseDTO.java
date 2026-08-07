package com.example.Baseera.dto.response;

import com.example.Baseera.entity.Attachment;
import com.example.Baseera.enums.DocumentType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Note: no storedFilePath field — the internal storage location is never exposed to the client. */

@Data
@NoArgsConstructor
public class AttachmentResponseDTO  {

    private Long id;
    private Long childId;
    private String originalFileName;
    private DocumentType documentType;
    private LocalDateTime createdAt;
    private boolean hasAnalysis;

    public static AttachmentResponseDTO fromEntity(Attachment entity) {

        AttachmentResponseDTO dto = new AttachmentResponseDTO();

        dto.setId(entity.getId());
        dto.setChildId(entity.getChild().getId());
        dto.setOriginalFileName(entity.getOriginalFileName());
        dto.setDocumentType(entity.getDocumentType());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }

    public static List<AttachmentResponseDTO> fromEntity(List<Attachment> entities) {

        List<AttachmentResponseDTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Attachment entity : entities) {
                dtos.add(fromEntity(entity));
            }
        }

        return dtos;
    }

}