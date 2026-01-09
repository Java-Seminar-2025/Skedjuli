package org.example.service.validator;

import lombok.RequiredArgsConstructor;
import org.example.model.dto.request.create.UserCreateRequest;
import org.example.model.dto.UserDto;
import org.example.model.enums.Role;
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
     * Validate registration request. Void API — throws IllegalArgumentException on failure.
     */
    public void validateRegister(UserCreateRequest req) {
        var request = Optional.ofNullable(req)
                .orElseThrow(() -> new IllegalArgumentException("Register request is null"));

        var email = Optional.ofNullable(request.email()).orElse("").trim();
        var password = Optional.ofNullable(request.password()).orElse("");
        var confirm = Optional.ofNullable(request.confirmPassword()).orElse("");
        var firstName = Optional.ofNullable(request.firstName()).orElse("").trim();
        var lastName = Optional.ofNullable(request.lastName()).orElse("").trim();
        var roleStr = Optional.ofNullable(request.role()).orElse("").trim();

        if (!StringUtils.hasText(email)) throw new IllegalArgumentException("Email is required");
        if (!StringUtils.hasText(password)) throw new IllegalArgumentException("Password is required");
        if (!StringUtils.hasText(confirm)) throw new IllegalArgumentException("Confirm password is required");
        if (!StringUtils.hasText(firstName) || !StringUtils.hasText(lastName))
            throw new IllegalArgumentException("First name and last name are required");

        if (!password.equals(confirm)) throw new IllegalArgumentException("Passwords do not match");

        if (password.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters long");

        final Role role;
        try {
            role = Role.fromString(roleStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + roleStr);
        }

        if (userDomainService.existsByEmail(email))
            throw new IllegalArgumentException("Email already exists");

        if (userDomainService.existsByUsername(email))
            throw new IllegalArgumentException("Username already exists");

        if (role == Role.STUDENT) {
            var sp = request.studyProgramId();
            if (sp <= 0L)
                throw new IllegalArgumentException("Study program id must be set for student");

            if (!studyProgramDomainService.existsById(sp))
                throw new IllegalArgumentException("Study program not found: " + sp);
        }
    }

    /**
     * Validate login request parameters (email + password). Void API.
     */
    public void validateLogin(String email, String password) {
        var e = Optional.ofNullable(email).orElse("").trim();
        var p = Optional.ofNullable(password).orElse("");

        if (!StringUtils.hasText(e) || !StringUtils.hasText(p))
            throw new IllegalArgumentException("Email and password are required");
    }

    /**
     * Validate that the provided raw password matches the stored password hash.
     * Void API — throws IllegalArgumentException on failure.
     */
    public void validatePassword(UserDto userDto, String rawPassword) {
        var user = Optional.ofNullable(userDto)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        var raw = Optional.ofNullable(rawPassword).orElse("");
        if (!StringUtils.hasText(raw))
            throw new IllegalArgumentException("Password is required");

        var hash = Optional.ofNullable(user.passwordHash()).orElse("");
        if (!passwordEncoder.matches(raw, hash))
            throw new IllegalArgumentException("Invalid email or password");
    }
}
