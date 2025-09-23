package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CartExceptionMP {
    public final String USER_CART_WAS_NOT_FOUND = "User cart with user id: %d not found";
    public final String EMPTY_USER_CART = "Cannot create order from empty cart";
    public final String ITEMS_NULL = "Cart items cannot be null";
    public final String ITEMS_EMPTY = "Cart items cannot be empty";
    public final String REMOVING_LIST_IS_NULL = "List is null";
    public final String REMOVING_LIST_IS_EMPTY = "List is empty ";
    public final String POSITIVE_REMOVING_ID = "Ids must be positive";
}
