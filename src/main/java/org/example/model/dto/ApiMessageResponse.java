package org.example.model.dto;

public record ApiMessageResponse(
        String status,
        String message
) {}