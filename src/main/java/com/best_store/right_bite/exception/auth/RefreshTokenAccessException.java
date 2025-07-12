package com.best_store.right_bite.exception.auth;

public class RefreshTokenAccessException extends RuntimeException{
    public RefreshTokenAccessException(String message) {
        super(message);
    }
}
