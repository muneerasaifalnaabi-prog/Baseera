package com.example.Baseera.dto.request;

import com.example.Baseera.enums.ProgressStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProgressRequestDTO {

    @NotNull(message = "Status is required")
    private ProgressStatus status;

    private String notes;
}
