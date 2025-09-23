package com.best_store.right_bite.exception.exceptions.auth;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }

}
