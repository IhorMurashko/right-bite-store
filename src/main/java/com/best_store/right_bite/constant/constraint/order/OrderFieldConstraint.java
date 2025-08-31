package com.best_store.right_bite.constant.constraint.order;

import lombok.experimental.UtilityClass;

@UtilityClass
public class OrderFieldConstraint {
    public final int MIN_ITEMS_QUANTITY = 1;
    public final int MAX_ITEMS_QUANTITY = 100;
    public final String ITEMS_QUANTITY_EXCEPTION_MESSAGE = "Quantity must be greater than 0 and less than 100";
}
