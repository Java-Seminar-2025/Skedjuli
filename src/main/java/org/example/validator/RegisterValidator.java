package org.example.validator;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.RegisterRequest;
import org.example.domain.enums.Role;
import org.example.service.domain.StudyProgramDomainService;
import org.example.service.domain.UserDomainService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterValidator {

    private final UserDomainService userDomainService;
    private final StudyProgramDomainService studyProgramDomainService;

    public void validate(RegisterRequest req) {
        if (req == null) throw new RuntimeException("Register request is null");

        if (req.email() == null || req.email().isBlank())
            throw new RuntimeException("Email cannot be empty");

        if (req.password() == null || req.password().isBlank())
            throw new RuntimeException("Password cannot be empty");

        if (req.firstName() == null || req.firstName().isBlank()
                || req.lastName() == null || req.lastName().isBlank())
            throw new RuntimeException("First name and last name are required");

        var role = Role.fromString(req.role()); // will throw on invalid role

        if (userDomainService.existsByEmail(req.email()))
            throw new RuntimeException("Email already exists");

        if (userDomainService.existsByUsername(req.email()))
            throw new RuntimeException("Username already exists");

        if (role == Role.STUDENT) {
            if (req.studyProgramId() <= 0)
                throw new RuntimeException("Study program id must be set for student");

            if (!studyProgramDomainService.existsById((long) req.studyProgramId()))
                throw new RuntimeException("Study program not found");
        }
    }
}
