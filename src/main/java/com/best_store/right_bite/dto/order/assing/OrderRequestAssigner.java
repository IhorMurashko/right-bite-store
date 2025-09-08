package com.best_store.right_bite.dto.order.assing;

import java.util.Collection;

public interface OrderRequestAssigner {
    Collection<? extends OrderItemsRequestAssigner> items();

    OrderDeliveryDetailsAssigner deliveryDetails();
}
