package org.example.exception;

public class AcademicYearNotFoundException extends RuntimeException {
    public AcademicYearNotFoundException(String message) {
        super(message);
    }
}