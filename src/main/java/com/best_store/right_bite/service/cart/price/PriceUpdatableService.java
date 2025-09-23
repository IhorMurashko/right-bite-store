package com.best_store.right_bite.service.cart.price;

import org.springframework.lang.NonNull;

/**
 * Functional interface for services responsible for updating product prices in a shopping cart.
 * <p>
 * This service defines a single method that recalculates and updates the cart items' prices for a specific user.
 * It uses product data to ensure pricing reflects the latest information.
 * </p>
 *
 * <p>Usage example:</p>
 * <pre><code>
 * int updatedItemsCount = priceUpdatableService.refreshCartPrices(userId);
 * </code></pre>
 *
 * @see PriceUpdatableServiceImpl
 * @author Ihor Murashko
 */
@FunctionalInterface
public interface PriceUpdatableService {
    /**
     * Functional interface for services responsible for updating product prices in a shopping cart.
     * <p>
     * This service defines a single method that recalculates and updates the prices of items
     * in the cart for a specified user. The method ensures the item prices reflect the latest
     * data from the product repository.
     * </p>
     *
     * <p>Implemented by:</p>
     * {@link PriceUpdatableServiceImpl}
     *
     * <p>Usage example:</p>
     * <pre>{@code
     * int modifiedItems = priceUpdatableService.refreshCartPrices(userId);
     * }</pre>
     *
     * @see PriceUpdatableServiceImpl
     * @see com.best_store.right_bite.repository.cart.CartRepository
     * @see com.best_store.right_bite.repository.catalog.ProductRepository
     * @see com.best_store.right_bite.exception.exceptions.cart.UserCartNotFoundException
     * @see com.best_store.right_bite.constant.constraint.products.ProductConstraints
     * @see com.best_store.right_bite.dto.catalog.product.ProductPriceDto
     *
     * @author Ihor Murashko
     */
    int refreshCartPrices(@NonNull Long userId);
}