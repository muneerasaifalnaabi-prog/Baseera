package com.example.Baseera.controller;

import com.example.Baseera.dto.request.AttachmentRequestDTO;
import com.example.Baseera.dto.response.AttachmentResponseDTO;
import com.example.Baseera.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/attachment")
public class AttachmentController {
    private final AttachmentService service;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AttachmentResponseDTO addAttachment(
            @RequestParam Long childId,
            @RequestParam MultipartFile file) {

        AttachmentRequestDTO dto = new AttachmentRequestDTO();
        dto.setChildId(childId);
        dto.setFile(file);

        return service.addAttachment(dto);
    }


    @GetMapping("{attachmentId}")
    public AttachmentResponseDTO getAttachmentById(@PathVariable Long attachmentId){
        return service.getAttachmentById(attachmentId);
    }

    @PostMapping("{attachmentId}")
    public AttachmentResponseDTO updateAttachment(@PathVariable Long attachmentId, @RequestBody AttachmentRequestDTO updatedDTO){
        return service.updateAttachment(attachmentId,updatedDTO);
    }

    @DeleteMapping("{attachmentId}")
    public String deleteAttachment(@PathVariable Long attachmentId){
        return service.deleteAttachment(attachmentId);
    }

}
