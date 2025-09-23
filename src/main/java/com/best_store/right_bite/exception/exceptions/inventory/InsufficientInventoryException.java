package com.best_store.right_bite.exception.exceptions.inventory;

public class InsufficientInventoryException extends RuntimeException{
    public InsufficientInventoryException(String message) {
        super(message);
    }
}
