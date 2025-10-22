package com.best_store.right_bite.constant.constraint.order;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderFieldConstraint {
    public final int MIN_ITEMS_QUANTITY = 1;
    public final int MAX_ITEMS_QUANTITY = 100;
    public final String ITEMS_QUANTITY_EXCEPTION_MESSAGE = "Quantity must be greater than 0 and less than 100";
    public final int MIN_SCALE_VALUE = 0;
    public final int MAX_SCALE_VALUE = 10;
    public final String NULL_ORDER_DELIVERY_DETAILS_EXCEPTION_MESSAGE = "Order delivery details must be provided";
    public final String NULL_ORDER_ITEMS_EXCEPTION_MESSAGE = "Order set must not be null";

}
