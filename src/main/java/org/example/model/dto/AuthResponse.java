package org.example.model.dto;

public record AuthResponse(
        String token,
        String email
) {}