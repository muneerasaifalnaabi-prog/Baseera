package com.example.Baseera.service;

import com.example.Baseera.exception.AiServiceException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
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
