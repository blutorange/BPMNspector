package com.github.blutorange.bpmnspector.api;

/** Exception thrown when a validation error occurs. */
public class ValidationException extends Exception {
    private static final long serialVersionUID = 5647199707160120246L;

    /** Constructs a new ValidationException without a detail message or cause. */
    public ValidationException() {
        super();
    }

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param throwable the cause
     */
    public ValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new ValidationException with the specified cause.
     *
     * @param throwable the cause
     */
    public ValidationException(Throwable throwable) {
        super(throwable);
    }
}
