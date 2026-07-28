package com.example.Baseera.service;

import com.example.Baseera.dto.response.AttachmentAnalysisResponseDTO;
import com.example.Baseera.entity.Attachment;
import com.example.Baseera.entity.AttachmentAnalysis;
import com.example.Baseera.exception.AiServiceException;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AttachmentAnalysisRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentAnalysisService {
    @Autowired
    private AttachmentAnalysisRepository analysisRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ChildService childService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private TextExtractionService textExtractionService;

    @Autowired
    private ChatClient chatClient;

    //****========
    // parent: trigger AI analysis of an already-uploaded report.
    // This saved result becomes the child's "current plan" — there's no
    // separate admin-authored plan catalog.
    //==========****
    public AttachmentAnalysisResponseDTO analyze(Long attachmentId, Long parentId) {
        Attachment attachment = attachmentService.getAttachmentOwnedByParent(attachmentId, parentId);

        // 1. Read the actual bytes back from disk using the saved path
        byte[] fileBytes = fileStorageService.loadAsBytes(attachment.getStoredFilePath());

        // 2. Convert those bytes into plain text Gemini can read
        String extractedText = textExtractionService.extractText(fileBytes);

        // 3. Send the text to Gemini and get structured JSON back
        AiAnalysisResult result = analyzeWithGemini(extractedText);

        // 4. Persist the structured result
        AttachmentAnalysis analysis = new AttachmentAnalysis();
        analysis.setAttachment(attachment);
        analysis.setImprovementSigns(result.improvementSigns());
        analysis.setProgressSummary(result.progressSummary());
        analysis.setSuggestedGoalTags(result.suggestedGoalTags());

        AttachmentAnalysis saved = analysisRepository.save(analysis);
        return AttachmentAnalysisResponseDTO.fromEntity(saved);
    }

    //****========
    // parent: "current plan" — the most recent AttachmentAnalysis across
    // ALL of the child's uploaded reports, not tied to one specific attachment
    //==========****
    public AttachmentAnalysisResponseDTO getCurrentPlanForChild(Long childId, Long parentId) {
        childService.getChildOwnedByParent(childId, parentId);

        AttachmentAnalysis latest = analysisRepository.findCurrentPlanForChild(childId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No analyzed report yet for this child — upload and analyze a report first"));
        return AttachmentAnalysisResponseDTO.fromEntity(latest);
    }

    private AiAnalysisResult analyzeWithGemini(String extractedText) {
        String prompt = """
                You are analyzing a specialist's report about a child with ASD/ADHD.
                Respond ONLY with JSON in this exact shape:
                {
                  "improvementSigns": "...",
                  "progressSummary": "...",
                  "suggestedGoalTags": ["tag1", "tag2"]
                }

                Report text:
                %s
                """.formatted(extractedText);

        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .entity(AiAnalysisResult.class);
        } catch (Exception e) {
            throw new AiServiceException("AI analysis is temporarily unavailable, please try again shortly");
        }
    }

    private record AiAnalysisResult(
            String improvementSigns,
            String progressSummary,
            List<String> suggestedGoalTags
    ) {
    }
}
