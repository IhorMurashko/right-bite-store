package com.best_store.right_bite.dto.cart.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object representing a shopping cart response.
 * Used to send cart details to the client.
 *
 * <p>Contains a set of cart items and the total price of the cart.</p>
 *
 * @see com.best_store.right_bite.model.cart.Cart
 */
public record CartResponseDto(
        Set<CartItemResponseDto> cartItems,
        BigDecimal totalPrice) implements Serializable {
}