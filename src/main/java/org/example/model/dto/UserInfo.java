package org.example.model.dto;

import org.example.model.enums.Role;

public record UserInfo(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {}