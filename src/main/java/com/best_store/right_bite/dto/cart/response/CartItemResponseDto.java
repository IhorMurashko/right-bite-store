package com.best_store.right_bite.dto.cart.response;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.best_store.right_bite.model.cart.CartItem}
 */
public record CartItemResponseDto(
        Long productId,
        String productName,
        int quantity,
        BigDecimal unitPriceSnapshot,
        BigDecimal totalPrice,
        String thumbnailUrl) implements Serializable {
}