package com.example.Baseera.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import com.example.Baseera.dto.request.AttachmentRequestDTO;
import com.example.Baseera.dto.response.AttachmentResponseDTO;
import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.Child;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AttachmentRepository;
import com.example.Baseera.repository.ChildRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final AttachmentRepository repository;
    private final ChildRepository childRepository;
    private final String UPLOAD_DIR = "uploads/";

    //create Attachment
   /* public AttachmentResponseDTO addAttachment(AttachmentRequestDTO dto) {

        Child child = childRepository.findById(dto.getChildId())
                .orElseThrow(() -> new ResourceNotFoundException("Child not found"));

        try {

            MultipartFile file = dto.getFile();

            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }

            // Allow only PDF file
            if (!file.getContentType().equals("application/pdf")) {
                throw new RuntimeException("Only PDF files are allowed");
            }

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            Attachment attachment = Attachment.builder()
                    .child(child)
                    .originalFileName(file.getOriginalFilename())
                    .storedFilePath(path.toString())
                    .type(file.getContentType())
                    .build();

            repository.save(attachment);

            return AttachmentResponseDTO.fromEntity(attachment);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file");
        }
    }*/

    //read Attachment
    public AttachmentResponseDTO getAttachmentById(Long attachmentId){

        Attachment attachment = repository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
        return AttachmentResponseDTO.fromEntity(attachment);
    }

    /*//update Attachment
    public AttachmentResponseDTO updateAttachment(Long attachmentId , AttachmentRequestDTO updatedDTO){
        Attachment attachment = repository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

        Child child = childRepository.findById(updatedDTO.getChildId())
                .orElseThrow(() -> new ResourceNotFoundException("Child not found"));

        attachment.setOriginalFileName(updatedDTO.getOriginalFileName());
        attachment.setType(updatedDTO.getType());
        attachment.setChild(child);
        attachment.setStoredFilePath(updatedDTO.getStoredFilePath());

        repository.save(attachment);
        return AttachmentResponseDTO.fromEntity(attachment);
    }*/

    //soft delete Attachment
    public  String deleteAttachment(Long attachmentId){
        Attachment attachment = repository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

        if(!attachment.getIsActive()){
            return "Attachment not found";
        }

        attachment.setIsActive(false);
        repository.save(attachment);
        return "Attachment deleted successfully";

    }



}
