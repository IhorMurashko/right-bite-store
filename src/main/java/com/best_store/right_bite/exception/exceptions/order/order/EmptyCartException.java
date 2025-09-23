package com.best_store.right_bite.exception.exceptions.order.order;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }
}
