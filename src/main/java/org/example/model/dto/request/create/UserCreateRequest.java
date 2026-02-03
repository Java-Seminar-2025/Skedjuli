package org.example.model.dto.request.create;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
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
        Long studyProgramId,
        Integer enrollmentYear,
        LocalDate dateOfBirth
) {}
