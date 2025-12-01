package org.example.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;
    private String confirmPassword;

    private String department;
    private String academicTitle;
    private String officeLocation;
    private String phoneNumber;

    private Long studyProgramId;
    private Integer enrollmentYear;
    private Integer currentYear;

    private LocalDate dateOfBirth;
}
