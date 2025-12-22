package org.example.model.dto;

public record AuthRequest(
        String email,
        String password
) {}