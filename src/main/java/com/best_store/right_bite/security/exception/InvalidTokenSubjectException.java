package com.best_store.right_bite.security.exception;

/**
 * Exception thrown when the subject of the token is invalid or cannot be parsed.
 * <p>
 * Typically used in scenarios where the JWT subject does not match the expected format
 * or cannot be converted to the required type.
 */
public class InvalidTokenSubjectException extends RuntimeException {
    public InvalidTokenSubjectException(String message) {
        super(message);
    }
}
