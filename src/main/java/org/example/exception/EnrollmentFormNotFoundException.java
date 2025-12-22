package org.example.exception;

public class EnrollmentFormNotFoundException extends RuntimeException {

    public EnrollmentFormNotFoundException(Long formId) {
        super("Enrollment form not found: id=" + formId);
    }

    public EnrollmentFormNotFoundException(Long studentId, int semester) {
        super("Enrollment form not found for studentId=" + studentId + " semester=" + semester);
    }
}