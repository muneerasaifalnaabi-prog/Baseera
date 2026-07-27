package com.example.Baseera.dto.request;
import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.Child;
import lombok.Data;

@Data
public class AttachmentRequestDTO {

    private Long childId;

    private String originalFileName;

    private String storedFilePath;

    private String type;

    public Attachment toEntity(Child child) {
        return Attachment.builder()
                .child(child)
                .originalFileName(originalFileName)
                .storedFilePath(storedFilePath)
                .type(type)
                .build();
    }
}
