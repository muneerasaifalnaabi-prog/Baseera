package com.example.Baseera.service;

import com.example.Baseera.dto.request.AttachmentRequestDTO;
import com.example.Baseera.dto.response.AttachmentResponseDTO;
import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.Child;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AttachmentRepository;
import com.example.Baseera.repository.ChildRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final AttachmentRepository repository;
    private final ChildRepository childRepository;

    //create Attachment
    public AttachmentResponseDTO addAttachment(AttachmentRequestDTO dto){

        Child child = childRepository.findById(dto.getChildId())
                .orElseThrow(() -> new ResourceNotFoundException("Child not exist"));

       Attachment attachment = dto.toEntity(child);
       repository.save(attachment);
       return AttachmentResponseDTO.fromEntity(attachment);
    }

    //read Attachment
    public AttachmentResponseDTO getAttachmentById(Long attachmentId){

        Attachment attachment = repository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not find"));
        return AttachmentResponseDTO.fromEntity(attachment);
    }

    //update Attachment
    public AttachmentResponseDTO updateAttachment(Long attachmentId , AttachmentRequestDTO updatedDTO){
        Attachment attachment = repository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not find"));

        Child child = childRepository.findById(updatedDTO.getChildId())
                .orElseThrow(() -> new ResourceNotFoundException("Child not exist"));

        attachment.setOriginalFileName(updatedDTO.getOriginalFileName());
        attachment.setType(updatedDTO.getType());
        attachment.setChild(child);
        attachment.setStoredFilePath(updatedDTO.getStoredFilePath());

        repository.save(attachment);
        return AttachmentResponseDTO.fromEntity(attachment);
    }



}
