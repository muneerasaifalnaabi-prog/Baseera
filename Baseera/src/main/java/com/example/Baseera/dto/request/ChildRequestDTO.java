package com.example.Baseera.dto.request;

import com.example.Baseera.entity.Account;
import com.example.Baseera.entity.Child;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Note: no age field — dateOfBirth is the only place age is ever entered.
 * Note: no parentId field — the owning parent is passed into toEntity()
 * from the authenticated JWT identity, never bound from this request body.
 */
@Data
@NoArgsConstructor
public class ChildRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private String gender;

    public Child toEntity(Account parent) {
        Child child = new Child();

        child.setFullName(fullName);
        child.setDateOfBirth(dateOfBirth);
        child.setGender(gender);
        child.setParent(parent);

        return child;
    }

    /** Update never changes the parent — ownership can't be reassigned this way. */
    public void applyTo(Child child) {
        child.setFullName(fullName);
        child.setDateOfBirth(dateOfBirth);
        child.setGender(gender);
    }
}
