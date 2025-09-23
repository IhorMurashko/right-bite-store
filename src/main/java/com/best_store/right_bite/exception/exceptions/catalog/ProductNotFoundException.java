package com.best_store.right_bite.exception.exceptions.catalog;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
