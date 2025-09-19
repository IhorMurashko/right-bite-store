package com.best_store.right_bite.service.cart.price;

import org.springframework.lang.NonNull;

@FunctionalInterface
public interface PriceUpdatableService {
    int refreshCartPrices(@NonNull Long userId);
}
