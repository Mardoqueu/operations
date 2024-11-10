package com.challenge.operations.exception;

/**
 * Exception thrown when an operation cannot be found in the system.
 *
 * This runtime exception is used to indicate that a requested operation does not exist or is not supported.
 *
 * @param message the detail message, which provides more information about the error.
 */
public class OperationNotFoundException extends RuntimeException {
    public OperationNotFoundException(String message) {
        super(message);
    }
}
