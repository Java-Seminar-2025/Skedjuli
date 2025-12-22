package org.example.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.model.dto.TargetRange;

@Getter
@AllArgsConstructor
public enum YearTarget {
    YEAR_1(60, 60),
    YEAR_2(58, 62),
    YEAR_3(58, 62); // YEAR_3 may extend to 80 if final paper is present

    private final int baseMin;
    private final int baseMax;

    public TargetRange getRange(boolean hasFinalPaper) {
        return this == YEAR_3
                ? new TargetRange(baseMin, hasFinalPaper ? 80 : baseMax)
                : new TargetRange(baseMin, baseMax);
    }

    public int min(boolean hasFinalPaper) {
        return getRange(hasFinalPaper).min();
    }

    public int max(boolean hasFinalPaper) {
        return getRange(hasFinalPaper).max();
    }

    public static YearTarget fromYear(int year) {
        return switch (year) {
            case 1 -> YEAR_1;
            case 2 -> YEAR_2;
            case 3 -> YEAR_3;
            default -> throw new IllegalArgumentException("Unsupported year: " + year);
        };
    }
}
