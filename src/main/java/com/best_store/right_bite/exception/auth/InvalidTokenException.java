package com.best_store.right_bite.exception.auth;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }

}
