package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * Provides predefined error messages related to order operations.
 *
 * <p>This utility class centralizes common order-related error messages,
 * ensuring consistency and maintainability across the application.</p>
 *
 * <p>Designed as a final utility class to avoid instantiation and inheritance.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new OrderException(String.format(OrderExceptionMP.NOT_ENOUGH_QUANTITY_IN_STOCK, productId));
 * }</pre>
 *
 * <p>Key error messages:</p>
 * <ul>
 *   <li>{@code NOT_ENOUGH_QUANTITY_IN_STOCK} - Error message for insufficient product stock.</li>
 *   <li>{@code ORDER_ID_NOT_FOUND} - Error message when the order ID is not found.</li>
 *   <li>{@code ORDER_ACCESS_DENIED} - Error message for unauthorized access to an order.</li>
 * </ul>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class OrderExceptionMP {
    public final String NOT_ENOUGH_QUANTITY_IN_STOCK = "Product with id: %d has not enough quantity in stock";
    public final String ORDER_ID_NOT_FOUND = "Order with ID: %d not found";
    public final String ORDER_ACCESS_DENIED = "Access denied to order: %d";
}