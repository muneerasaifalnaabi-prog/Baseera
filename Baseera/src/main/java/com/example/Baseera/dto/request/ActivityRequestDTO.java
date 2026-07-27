package com.example.Baseera.dto.request;


import com.example.Baseera.entity.Activity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Minimum age is required")
    @Min(value = 0, message = "Minimum age cannot be negative")
    @Max(value = 18, message = "Minimum age is out of range")
    private Integer minAge;

    @NotNull(message = "Maximum age is required")
    @Min(value = 0, message = "Maximum age cannot be negative")
    @Max(value = 18, message = "Maximum age is out of range")
    private Integer maxAge;

    @NotBlank(message = "Target condition is required")
    @Pattern(regexp = "ASD|ADHD", message = "Target condition must be ASD or ADHD")
    private String targetCondition;

    public Activity toEntity() {
        Activity activity = new Activity();

        activity.setName(name);
        activity.setDescription(description);
        activity.setMinAge(minAge);
        activity.setMaxAge(maxAge);
        activity.setTargetCondition(targetCondition);

        return activity;
    }

    public void applyTo(Activity activity) {
        activity.setName(name);
        activity.setDescription(description);
        activity.setMinAge(minAge);
        activity.setMaxAge(maxAge);
        activity.setTargetCondition(targetCondition);
    }
}
