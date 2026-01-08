package org.example.model.dto.request.create;

public record LecturerCreateRequest(
        Long userId,
        String department,
        String title,
        String office,
        String phone
) {}
