package org.example.model.dto.response;

import org.example.model.enums.Role;

public record UserResponse(
        Long id,
        String email,
        String username,
        String firstName,
        String lastName,
        Role role
) {}