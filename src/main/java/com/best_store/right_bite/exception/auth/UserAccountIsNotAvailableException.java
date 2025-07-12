package com.best_store.right_bite.exception.auth;

public class UserAccountIsNotAvailableException extends RuntimeException {
    public UserAccountIsNotAvailableException(String message) {
        super(message);
    }
}
