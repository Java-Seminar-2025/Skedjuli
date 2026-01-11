package org.example.model.dto.request.create;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        @NotNull Long studyProgramId,
        Integer enrollmentYear,
        //enrloment year maknit
        //current year hardkodirat da je 1 jer ka upisujes se
        //odbarat smjer
        Integer currentYear,
        LocalDate dateOfBirth
) {}
