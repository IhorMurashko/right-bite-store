package com.best_store.right_bite.exception.exceptions.order.access;

public class OrderAccessDeniedException extends RuntimeException{
    public OrderAccessDeniedException(String message) {
        super(message);
    }
}
