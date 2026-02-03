package org.example.model.dto;

import org.example.model.dto.response.UserResponse;

public record AuthLoginResponse(
        UserResponse user,
        Long studentId,
        Long lecturerId) {
}
