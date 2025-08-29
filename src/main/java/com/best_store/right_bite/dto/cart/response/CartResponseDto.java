package com.best_store.right_bite.dto.cart.response;

import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(name = "cart response")
public record CartResponseDto(
        @Schema(description = "list of items in the cart")
        Set<CartItemResponseDto> cartItems,
        @Schema(description = "total price")
        BigDecimal totalPrice) implements Serializable {
}