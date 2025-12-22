package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.RegisterRequest;
import org.example.model.dto.UserDto;
import org.example.model.enums.Role;
import org.example.exception.EnrollmentValidationException;
import org.example.service.domain.StudyProgramDomainService;
import org.example.service.domain.UserDomainService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final UserDomainService userDomainService;
    private final StudyProgramDomainService studyProgramDomainService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Validate registration request. Void API — throws EnrollmentValidationException on failure.
     */
    public void validateRegister(RegisterRequest req) {
        var request = Optional.ofNullable(req)
                .orElseThrow(() -> new EnrollmentValidationException("Register request is null"));

        var email = Optional.ofNullable(request.email()).orElse("").trim();
        var password = Optional.ofNullable(request.password()).orElse("");
        var confirm = Optional.ofNullable(request.confirmPassword()).orElse("");
        var firstName = Optional.ofNullable(request.firstName()).orElse("").trim();
        var lastName = Optional.ofNullable(request.lastName()).orElse("").trim();
        var roleStr = Optional.ofNullable(request.role()).orElse("").trim();

        if (!StringUtils.hasText(email)) throw new EnrollmentValidationException("Email is required");
        if (!StringUtils.hasText(password)) throw new EnrollmentValidationException("Password is required");
        if (!StringUtils.hasText(confirm)) throw new EnrollmentValidationException("Confirm password is required");
        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName))
            throw new EnrollmentValidationException("First name and last name are required");

        if (!password.equals(confirm)) throw new EnrollmentValidationException("Passwords do not match");

        // Example extra checks: password length (adjust policy as needed)
        if (password.length() < 8) throw new EnrollmentValidationException("Password must be at least 8 characters long");

        // Validate role
        final Role role;
        try {
            role = Role.fromString(roleStr);
        } catch (Exception e) {
            throw new EnrollmentValidationException("Invalid role: " + roleStr);
        }

        // Unique constraints
        if (userDomainService.existsByEmail(email)) throw new EnrollmentValidationException("Email already exists");
        if (userDomainService.existsByUsername(email)) throw new EnrollmentValidationException("Username already exists");

        // Student-specific checks
        if (role == Role.STUDENT) {
            var sp = request.studyProgramId(); // supports both int or long in record
            if ((long) sp <= 0L) throw new EnrollmentValidationException("Study program id must be set for student");
            if (!studyProgramDomainService.existsById((long) sp))
                throw new EnrollmentValidationException("Study program not found: " + sp);
        }
    }

    /**
     * Validate login request parameters (email + password). Void API.
     */
    public void validateLogin(String email, String password) {
        var e = Optional.ofNullable(email).orElse("").trim();
        var p = Optional.ofNullable(password).orElse("");

        if (!StringUtils.hasText(e) || !StringUtils.hasText(p))
            throw new EnrollmentValidationException("Email and password are required");
    }

    /**
     * Validate that the provided raw password matches the stored password hash.
     * Void API — throws EnrollmentValidationException on failure.
     */
    public void validatePassword(UserDto userDto, String rawPassword) {
        var user = Optional.ofNullable(userDto).orElseThrow(() -> new EnrollmentValidationException("Invalid email or password"));
        var raw = Optional.ofNullable(rawPassword).orElse("");
        if (!StringUtils.hasText(raw)) throw new EnrollmentValidationException("Password is required");

        var hash = Optional.ofNullable(user.passwordHash()).orElse("");
        if (!passwordEncoder.matches(raw, hash)) throw new EnrollmentValidationException("Invalid email or password");
    }
}
