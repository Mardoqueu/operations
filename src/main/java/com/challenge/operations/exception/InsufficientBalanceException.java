package com.challenge.operations.exception;

/**
 * Exception thrown when a user's balance is insufficient to complete an operation.
 *
 * This runtime exception is typically used in financial or transaction processing
 * applications where operations could fail due to a lack of funds.
 *
 * @param message a detailed message about the cause of the exception
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
