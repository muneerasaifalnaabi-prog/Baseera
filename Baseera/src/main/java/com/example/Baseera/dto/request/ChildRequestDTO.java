package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Child;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ChildRequestDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotNull(message = "Parent ID is required")
    private Long parentId;

    @NotNull(message = "Plan ID is required")
    private Long planId;

    public Child toEntity() {

        Child child = new Child();

        child.setFullName(fullName);
        child.setDateOfBirth(dateOfBirth);
        child.setGender(gender);

        return child;
    }

    public void applyTo(Child child) {

        child.setFullName(fullName);
        child.setDateOfBirth(dateOfBirth);
        child.setGender(gender);
    }
}
