package com.challenge.operations.exception;

/**
 * Exception thrown when a user cannot be found in the system.
 *
 * This runtime exception is typically used in services or controllers
 * to indicate that a requested user does not exist.
 *
 * @param message a detailed message about the cause of the exception
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
