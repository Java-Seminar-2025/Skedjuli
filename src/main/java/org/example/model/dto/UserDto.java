package org.example.model.dto;

import org.example.model.enums.Role;

public record UserDto(
        Long id,
        String email,
        String passwordHash,
        String firstName,
        String lastName,
        Role role
) {}