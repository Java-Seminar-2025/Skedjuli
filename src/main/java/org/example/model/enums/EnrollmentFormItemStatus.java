package org.example.model.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum EnrollmentFormItemStatus {
    PENDING(1),
    APPROVED(2),
    REJECTED(3);

    private final int value;

    public static EnrollmentFormItemStatus fromValue(int value) {
        return switch (value) {
            case 1 -> PENDING;
            case 2 -> APPROVED;
            case 3 -> REJECTED;
            default -> throw new IllegalArgumentException("Invalid status value: " + value);
        };
    }
}
