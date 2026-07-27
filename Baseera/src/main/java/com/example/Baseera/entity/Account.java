package com.example.Baseera.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account  extends BaseEntity{
    private String email;
    private String password;
    private String fullName;
    private String role;
}
