package com.example.Baseera.dto.response;
import com.example.Baseera.entity.Attachment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AttachmentResponseDTO {
    private Long id;

    private Long childId;

    private String originalFileName;

    private String storedFilePath;

    private String type;

    private Boolean isActive;

    public static AttachmentResponseDTO fromEntity(Attachment attachment) {
        return AttachmentResponseDTO.builder()
                .id(attachment.getId())
                .childId(attachment.getChild().getId())
                .originalFileName(attachment.getOriginalFileName())
                .storedFilePath(attachment.getStoredFilePath())
                .type(attachment.getType())
                .isActive(attachment.getIsActive())
                .build();
    }
}
