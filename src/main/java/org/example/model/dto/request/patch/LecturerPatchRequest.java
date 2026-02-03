package org.example.model.dto.request.patch;

import jakarta.validation.constraints.Size;

public record LecturerPatchRequest(
        @Size String department,
        @Size(max = 100) String title,
        @Size(max = 30) String office,
        @Size(max = 20) String phone,
        Boolean isActive
) {
}
