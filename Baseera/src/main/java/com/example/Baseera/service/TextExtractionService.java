package com.example.Baseera.service;

import com.example.Baseera.exception.AiServiceException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

/**
 * Gemini reads plain text, not raw PDF/DOCX bytes. This converts whatever
 * the specialist handed the parent into a text string, which is what
 * actually gets sent to the AI in AttachmentAnalysisService.analyze(...).
 *
 * pom.xml:
 *   <dependency>
 *       <groupId>org.apache.tika</groupId>
 *       <artifactId>tika-core</artifactId>
 *       <version>2.9.2</version>
 *   </dependency>
 *   <dependency>
 *       <groupId>org.apache.tika</groupId>
 *       <artifactId>tika-parsers-standard-package</artifactId>
 *       <version>2.9.2</version>
 *   </dependency>
 */
@Service
public class TextExtractionService {

    private final Tika tika = new Tika();

    public String extractText(byte[] fileBytes) {
        try {
            String text = tika.parseToString(new java.io.ByteArrayInputStream(fileBytes));
            if (text == null || text.isBlank()) {
                throw new AiServiceException("No readable text found in the uploaded report");
            }
            return text;
        } catch (AiServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new AiServiceException("Failed to extract text from attachment: " + e.getMessage());
        }
    }
}
