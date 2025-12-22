package org.example.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum Role {
    STUDENT(1),
    PROFESSOR(2),
    ADMIN(3);

    private final int value;

    public static Role fromString(String roleString) {
        if (roleString == null) throw new IllegalArgumentException("Role cannot be null");
        return switch (roleString.trim().toLowerCase()) {
            case "professor", "lecturer", "teacher" -> PROFESSOR;
            case "student", "learner" -> STUDENT;
            case "admin" -> ADMIN;
            default -> throw new IllegalArgumentException("Unknown role: " + roleString);
        };
    }
}
