package org.example.domain.dto;

public record AuthResponse(
        String token,
        String email
) {}