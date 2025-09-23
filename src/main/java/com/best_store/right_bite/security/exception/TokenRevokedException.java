package com.best_store.right_bite.security.exception;

/**
 * Exception thrown when a token has been revoked and is no longer valid.
 * <p>
 * Typically used to handle scenarios where attempts are made to use a security token,
 * such as a JWT, that has been explicitly revoked.
 */
public class TokenRevokedException extends RuntimeException {
    public TokenRevokedException(String message) {
        super(message);
    }
}