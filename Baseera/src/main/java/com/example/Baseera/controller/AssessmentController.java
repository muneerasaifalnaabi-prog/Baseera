package com.example.Baseera.controller;

import com.example.Baseera.dto.request.AssessmentRequestDTO;
import com.example.Baseera.dto.response.AssessmentResponseDTO;
import com.example.Baseera.security.SecurityUtils;
import com.example.Baseera.service.AssessmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/children/{childId}/assessments")
public class AssessmentController {
    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private SecurityUtils securityUtils;

    // parent: submit a free-text behavior description; Gemini returns riskLevel + suggestedCondition
    @PostMapping
    public ResponseEntity<AssessmentResponseDTO> createAssessment(
            @PathVariable Long childId, @Valid @RequestBody AssessmentRequestDTO dto) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.status(HttpStatus.CREATED).body(
                assessmentService.createAssessment(childId, dto, parentId));
    }

    // parent: full assessment history for this child, most recent first
    @GetMapping
    public ResponseEntity<List<AssessmentResponseDTO>> getAssessments(@PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(assessmentService.getAssessmentsForChild(childId, parentId));
    }

}
