package org.example.model.dto;

public record UserInfo(
        Long id,
        String email,
        String firstName,
        String lastName,
        int role
) {}