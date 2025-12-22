package org.example.model.dto;

public record UserDto(
        Long id,
        String email,
        String passwordHash,
        String firstName,
        String lastName,
        int role
) {}