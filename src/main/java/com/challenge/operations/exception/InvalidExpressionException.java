package com.challenge.operations.exception;


/**
 * Exception thrown to indicate that an invalid expression has been encountered.
 * This class is a subclass of {@code RuntimeException}.
 *
 * <p>Use this exception to signal various types of invalid expressions such as:
 * <ul>
 * <li>Consecutive operators</li>
 * <li>Mismatched parentheses</li>
 * <li>Invalid tokens</li>
 * <li>Division by zero</li>
 * <li>Insufficient values for an operator</li>
 * <li>Extraneous values in an expression</li>
 * </ul>
 *
 * @param message Description of the reason why the exception is thrown
 */
public class InvalidExpressionException extends RuntimeException {
    public InvalidExpressionException(String message) {
        super(message);
    }
}