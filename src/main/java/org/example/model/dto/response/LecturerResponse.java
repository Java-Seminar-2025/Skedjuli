package org.example.model.dto.response;

public record LecturerResponse(
      Long id,
      Long userId,
      String department,
      String title,
      String office,
      String phone,
      Boolean isActive
) {}
