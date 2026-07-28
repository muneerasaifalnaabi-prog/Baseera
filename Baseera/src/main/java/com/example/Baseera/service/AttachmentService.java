package com.example.Baseera.service;

import com.example.Baseera.dto.request.AttachmentRequestDTO;
import com.example.Baseera.dto.response.AttachmentResponseDTO;
import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.Child;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private ChildService childService;

    @Autowired
    private FileStorageService fileStorageService;

    //****========
    // parent: upload the specialist's report as a file, tagged with a DocumentType.
    // Ownership-checked the same way as every other child-linked write.
    //==========****

    public AttachmentResponseDTO uploadAttachment(Long childId, AttachmentRequestDTO dto, MultipartFile file, Long parentId) {
        Child child = childService.getChildOwnedByParent(childId, parentId);

        String originalFileName = fileStorageService.extractOriginalFileName(file);
        String storedFilePath = fileStorageService.store(file);

        Attachment attachment = dto.toEntity(child, originalFileName, storedFilePath);
        Attachment saved = attachmentRepository.save(attachment);

        return AttachmentResponseDTO.fromEntity(saved);
    }

    //****========
    // parent: list all reports uploaded for a child
    //==========****
    public List<AttachmentResponseDTO> getAttachmentsForChild(Long childId, Long parentId) {
        childService.getChildOwnedByParent(childId, parentId);
        List<Attachment> attachments = attachmentRepository.findAllByChildId(childId);
        return AttachmentResponseDTO.fromEntity(attachments);
    }

    //****========
    // parent: soft-delete an uploaded report (also removes the file from disk)
    //==========****
    public String deleteAttachment(Long attachmentId, Long parentId) {
        Attachment attachment = getAttachmentOwnedByParent(attachmentId, parentId);
        attachment.setIsActive(false);
        attachmentRepository.save(attachment);
        fileStorageService.delete(attachment.getStoredFilePath());
        return "DELETED";
    }

    //****========
    // shared helper — used by AttachmentAnalysisService.analyze(...) too
    //==========****
    public Attachment getAttachmentOwnedByParent(Long attachmentId, Long parentId) {
        Attachment attachment = attachmentRepository.findAttachmentById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found: " + attachmentId));

        if (!attachment.getChild().getParent().getId().equals(parentId)) {
            throw new ResourceNotFoundException("Attachment not found: " + attachmentId);
        }

        return attachment;
    }



}
