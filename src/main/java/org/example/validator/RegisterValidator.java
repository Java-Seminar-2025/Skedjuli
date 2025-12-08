package org.example.validator;

import org.example.domain.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class RegisterValidator {

    public void validate(RegisterRequest req) {
        if (req.email() == null || req.email().isBlank())
            throw new RuntimeException("Email cannot be empty");

        if (req.password() == null || req.password().isBlank())
            throw new RuntimeException("Password cannot be empty");

        if (req.firstName() == null || req.firstName().isBlank()
                || req.lastName() == null || req.lastName().isBlank())
            throw new RuntimeException("First name and last name are required");

        if (!req.role().equalsIgnoreCase("professor") &&
                !req.role().equalsIgnoreCase("student"))
            throw new RuntimeException("Invalid role");
    }
}