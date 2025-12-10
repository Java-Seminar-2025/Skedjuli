package org.example.domain.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String email,
        @NotBlank String role,
        @NotBlank String password,
        @NotBlank String confirmPassword,
        String department,
        String academicTitle,
        String officeLocation,
        String phoneNumber,
        @NotNull @Min(1) Long studyProgramId,
        @NotNull @Min(1) Integer enrollmentYear,
        @NotNull @Min(1) Integer currentYear,
        LocalDate dateOfBirth
) {}
