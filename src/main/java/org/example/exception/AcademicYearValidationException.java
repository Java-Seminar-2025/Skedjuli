package org.example.exception;

public class AcademicYearValidationException extends RuntimeException {
    public AcademicYearValidationException(String message) {
        super(message);
    }
}