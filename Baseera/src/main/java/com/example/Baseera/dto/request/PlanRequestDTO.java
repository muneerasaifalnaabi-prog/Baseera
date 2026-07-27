package com.example.Baseera.dto.request;


import com.example.Baseera.entity.Plans;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class PlanRequestDTO {

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Condition is required")
    @Pattern(regexp = "ASD|ADHD", message = "Condition must be ASD or ADHD")
    private String conditions;

    @NotNull(message = "Minimum age (months) is required")
    @Min(value = 0, message = "Minimum age cannot be negative")
    @Max(value = 216, message = "Minimum age is out of range")
    private Integer minAgeMonths;

    @NotNull(message = "Maximum age (months) is required")
    @Min(value = 0, message = "Maximum age cannot be negative")
    @Max(value = 216, message = "Maximum age is out of range")
    private Integer maxAgeMonths;

    public Plans toEntity() {
        Plans plans = new Plans();

        plans.setDescription(description);
        plans.setCategory(category);
        plans.setConditions(conditions);
        plans.setMinAgeMonths(minAgeMonths);
        plans.setMaxAgeMonths(maxAgeMonths);

        return plans;
    }

    public void applyTo(Plans plans) {
        plans.setDescription(description);
        plans.setCategory(category);
        plans.setConditions(conditions);
        plans.setMinAgeMonths(minAgeMonths);
        plans.setMaxAgeMonths(maxAgeMonths);
    }
}