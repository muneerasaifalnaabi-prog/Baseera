package com.example.Baseera.controller;

import com.example.Baseera.dto.request.AttachmentRequestDTO;
import com.example.Baseera.dto.response.AttachmentAnalysisResponseDTO;
import com.example.Baseera.dto.response.AttachmentResponseDTO;
import com.example.Baseera.enums.DocumentType;
import com.example.Baseera.security.SecurityUtils;
import com.example.Baseera.service.AttachmentAnalysisService;
import com.example.Baseera.service.AttachmentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentAnalysisService attachmentAnalysisService;

    @Autowired
    private SecurityUtils securityUtils;

    // parent: upload the specialist's report as a multipart file
    @PostMapping(value = "/api/children/{childId}/attachments", consumes = "multipart/form-data")
    public ResponseEntity<AttachmentResponseDTO> uploadAttachment(
            @PathVariable Long childId,
            @RequestParam("documentType") DocumentType documentType,
            @RequestParam("file") MultipartFile file) {

        Long parentId = securityUtils.getCurrentAccountId();
        AttachmentRequestDTO dto = new AttachmentRequestDTO();
        dto.setDocumentType(documentType);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                attachmentService.uploadAttachment(childId, dto, file, parentId));
    }

    // parent: list all reports uploaded for a child
    @GetMapping("/api/children/{childId}/attachments")
    public ResponseEntity<List<AttachmentResponseDTO>> getAttachments(@PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(attachmentService.getAttachmentsForChild(childId, parentId));
    }

    // parent: soft-delete an uploaded report
    @DeleteMapping("/api/attachments/{id}")
    public ResponseEntity<String> deleteAttachment(@PathVariable Long id) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(attachmentService.deleteAttachment(id, parentId));
    }

    // parent: trigger AI analysis of an uploaded report — this becomes the child's follow-up plan
    @PostMapping("/api/attachments/{id}/analyze")
    public ResponseEntity<AttachmentAnalysisResponseDTO> analyze(@PathVariable Long id) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(attachmentAnalysisService.analyze(id, parentId));
    }

    // parent: the child's current plan — most recent AttachmentAnalysis across all reports
    @GetMapping("/api/children/{childId}/current-plan")
    public ResponseEntity<AttachmentAnalysisResponseDTO> getCurrentPlan(@PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(attachmentAnalysisService.getCurrentPlanForChild(childId, parentId));
    }

    // parent: view analysis for a specific attachment
    @GetMapping("/api/attachments/{id}/analysis")
    public ResponseEntity<AttachmentAnalysisResponseDTO> getAnalysis(@PathVariable Long id) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(
                attachmentAnalysisService.getAnalysis(id, parentId)
        );
    }
}
