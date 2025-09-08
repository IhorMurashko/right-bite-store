package com.best_store.right_bite.exception.order.delivery;

public class EmptyDeliveryDetailsException extends RuntimeException{
    public EmptyDeliveryDetailsException(String message) {
        super(message);
    }
}
