package org.example.domain.dto;

import java.time.LocalDate;

public record RegisterRequest(
        String firstName,
        String lastName,
        String email,
        String role,
        String password,
        String confirmPassword,
        String department,
        String academicTitle,
        String officeLocation,
        String phoneNumber,
        Long studyProgramId,
        Integer enrollmentYear,
        Integer currentYear,
        LocalDate dateOfBirth
) {}