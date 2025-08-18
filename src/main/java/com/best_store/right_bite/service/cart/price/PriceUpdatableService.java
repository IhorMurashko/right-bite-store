package com.best_store.right_bite.service.cart.price;

import com.best_store.right_bite.model.cart.Cart;
import org.springframework.lang.NonNull;
/**
 * Service interface for updating prices in a cart based on current product prices.
 */
public interface PriceUpdatableService {
    /**
     * Refreshes prices for all items in the provided cart if their snapshot is expired.
     *
     * @param cart the cart whose item prices should be updated
     * @return number of items whose prices were modified
     */
    int refreshCartPrices(@NonNull Cart cart);
}
