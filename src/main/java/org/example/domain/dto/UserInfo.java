package org.example.domain.dto;

public record UserInfo(
        Long id,
        String email,
        String firstName,
        String lastName,
        int role
) {}