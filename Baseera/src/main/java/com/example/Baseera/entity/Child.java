package com.example.Baseera.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Child extends BaseEntity {
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;

}
