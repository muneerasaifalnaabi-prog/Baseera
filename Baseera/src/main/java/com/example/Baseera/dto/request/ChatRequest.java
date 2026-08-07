package com.example.Baseera.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatRequest(

        @NotBlank(message = "Message must not be empty")
        @Size(max = 2000, message = "Message is too long")
        String message,
        Long childId,

        String lang
) {}