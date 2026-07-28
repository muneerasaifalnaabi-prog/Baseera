package com.example.Baseera.service;


import com.example.Baseera.dto.request.AssessmentRequestDTO;
import com.example.Baseera.dto.response.AssessmentResponseDTO;
import com.example.Baseera.entity.Assessment;
import com.example.Baseera.entity.Child;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.enums.RiskLevel;
import com.example.Baseera.exception.AiServiceException;
import com.example.Baseera.repository.AssessmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ChildService childService;

    @Autowired
    private ChatClient chatClient;

    //****========
    // parent: submit a free-text behavior description for a child.
    // Ownership is checked FIRST (via ChildService) before anything else happens.
    //==========****
    public AssessmentResponseDTO createAssessment(Long childId, AssessmentRequestDTO dto, Long parentId) {
        Child child = childService.getChildOwnedByParent(childId, parentId);

        Assessment assessment = dto.toEntity(child);

        AiAssessmentResult result = analyzeWithGemini(dto.getDescription());
        assessment.setRiskLevel(result.riskLevel());
        assessment.setSuggestedCondition(result.suggestedCondition());

        Assessment saved = assessmentRepository.save(assessment);
        return AssessmentResponseDTO.fromEntity(saved);
    }

    //****========
    // parent: full assessment history for a child, most recent first
    //==========****
    public List<AssessmentResponseDTO> getAssessmentsForChild(Long childId, Long parentId) {
        childService.getChildOwnedByParent(childId, parentId);
        List<Assessment> assessments = assessmentRepository.findAllByChildIdOrderByCreatedAtDesc(childId);
        return AssessmentResponseDTO.fromEntity(assessments);
    }

    private AiAssessmentResult analyzeWithGemini(String description) {
        String prompt = """
                You are screening a free-text behavior description for a child.
                Respond ONLY with JSON in this exact shape:
                {
                  "riskLevel": "LOW" | "MEDIUM" | "HIGH",
                  "suggestedCondition": "ASD" | "ADHD"
                }

                Behavior description:
                %s
                """.formatted(description);

        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .entity(AiAssessmentResult.class);
        } catch (Exception e) {
            // Graceful fallback rather than a crash — surfaced to the client as 503.
            throw new AiServiceException("Assessment analysis is temporarily unavailable, please try again shortly");
        }
    }

    private record AiAssessmentResult(RiskLevel riskLevel, ConditionType suggestedCondition) {
    }


}
