package com.best_store.right_bite.security.exception;

/**
 * Exception thrown when the token type is invalid or does not meet the expected criteria.
 * <p>
 * Typically used in scenarios where the token's type, such as access or refresh,
 * is null or mismatches the required type for processing.
 */
public class InvalidTokenTypeException extends RuntimeException {
    public InvalidTokenTypeException(String message) {
        super(message);
    }
}