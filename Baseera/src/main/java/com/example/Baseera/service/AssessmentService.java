package com.example.Baseera.service;

import com.example.Baseera.dto.request.AssessmentRequestDTO;
import com.example.Baseera.dto.response.AssessmentResponseDTO;
import com.example.Baseera.entity.Assessment;
import com.example.Baseera.entity.Child;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.enums.RiskLevel;
import com.example.Baseera.exception.AiServiceException;
import com.example.Baseera.repository.AssessmentRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ChildService childService;

    private final ChatClient chatClient;

    // Scoped system instruction for THIS screening task only — separate from
    // GeminiController's free-chat instruction, since this call must always
    // return strict JSON, never conversational text.
    private static final String SYSTEM_INSTRUCTION = """
            You are a clinical screening assistant for an Autism Support App.
            You read a parent's free-text description of their child's behavior
            and return a structured risk screening. You are NOT a diagnostic tool
            and must never claim to diagnose — only flag risk level and suggest
            which condition (ASD or ADHD) warrants professional evaluation.
            Respond ONLY with JSON, no other text.
            """;

    public AssessmentService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_INSTRUCTION)
                .build();
    }

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
