package org.example.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum EnrollmentFormStatus {
    PENDING(1),
    APPROVED(2),
    REJECTED(3),
    LOCKED(4);

    private final int value;

    public static EnrollmentFormStatus fromValue(int value) {
        return switch (value) {
            case 1 -> PENDING;
            case 2 -> APPROVED;
            case 3 -> REJECTED;
            case 4 -> LOCKED;
            default -> throw new IllegalArgumentException("Invalid status value: " + value);
        };
    }
}