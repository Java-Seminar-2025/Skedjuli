package org.example.model.dto.request.patch;

public record StudentPatchRequest(
        Integer currentYear,
        Boolean isActive
) {}
