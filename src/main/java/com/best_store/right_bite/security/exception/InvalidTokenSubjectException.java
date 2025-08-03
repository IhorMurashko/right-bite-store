package com.best_store.right_bite.security.exception;

public class InvalidTokenSubjectException extends RuntimeException {
    public InvalidTokenSubjectException(String message) {
        super(message);
    }
}
