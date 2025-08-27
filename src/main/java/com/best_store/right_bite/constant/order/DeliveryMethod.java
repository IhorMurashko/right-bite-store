package com.best_store.right_bite.constant.order;

import lombok.Getter;

@Getter
public enum DeliveryMethod {
    HOME_DELIVERY("Home delivery"),
    COURIER("Courier"),
    PICKUP("Pick up");
    private final String deliveryMethod;

    DeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }
}
