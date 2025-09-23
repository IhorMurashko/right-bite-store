package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderExceptionMP {
    public final String NOT_ENOUGH_QUANTITY_IN_STOCK = "Product with id: %d has not enough quantity in stock";
    public final String ORDER_ID_NOT_FOUND = "Order with ID: %d not found";
    public final String ORDER_ACCESS_DENIED = "Access denied to order: %d";
}
