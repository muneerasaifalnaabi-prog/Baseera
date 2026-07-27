package com.example.Baseera.controller;

import com.example.Baseera.dto.request.AttachmentRequestDTO;
import com.example.Baseera.dto.response.AttachmentResponseDTO;
import com.example.Baseera.entity.Child;
import com.example.Baseera.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/attachment")
public class AttachmentController {
    private final AttachmentService service;


    @PostMapping
    public AttachmentResponseDTO addAttachment(@RequestBody AttachmentRequestDTO dto){
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


}
