package org.example.exception;

/** Thrown when enrollment validation fails (mapped to HTTP 400 by controller advice). */
public class EnrollmentValidationException extends RuntimeException {
    public EnrollmentValidationException(String message) { super(message); }
    public EnrollmentValidationException(String message, Throwable cause) { super(message, cause); }
}
