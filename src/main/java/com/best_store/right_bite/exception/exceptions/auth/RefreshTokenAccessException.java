package com.best_store.right_bite.exception.exceptions.auth;

public class RefreshTokenAccessException extends RuntimeException{
    public RefreshTokenAccessException(String message) {
        super(message);
    }
}
