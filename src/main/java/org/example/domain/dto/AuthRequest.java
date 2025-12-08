package org.example.domain.dto;

public record AuthRequest(
        String email,
        String password
) {}