package org.example.model.dto.request.create;

import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

public record LecturerCreateRequest(
        @NotNull Long userId,
        @Size String department,
        @Size(max = 100) String title,
        @Size(max = 30) String office,
        @Size(max = 20) String phone
) {}
