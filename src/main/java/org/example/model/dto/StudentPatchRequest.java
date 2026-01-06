package org.example.model.dto;

public record StudentPatchRequest(
        Integer currentYear,
        Boolean isActive
) { }
