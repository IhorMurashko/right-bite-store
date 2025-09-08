package com.best_store.right_bite.dto.order.assing;

import java.math.BigDecimal;

public interface OrderItemsRequestAssigner {
    Long productId();

    int quantity();

    String productName();

    BigDecimal priceSnapshot();
}
