package org.example.exception;

public class PrerequisiteCycleException extends RuntimeException {
    public PrerequisiteCycleException() { super(); }
    public PrerequisiteCycleException(String message) { super(message); }
    public PrerequisiteCycleException(String message, Throwable cause) { super(message, cause); }
    public PrerequisiteCycleException(Throwable cause) { super(cause); }
}
