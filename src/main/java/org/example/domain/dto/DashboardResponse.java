package org.example.domain.dto;

public record DashboardResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Integer role
) {}