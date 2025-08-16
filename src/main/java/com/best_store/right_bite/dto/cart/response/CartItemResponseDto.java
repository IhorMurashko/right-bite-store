package com.best_store.right_bite.dto.cart.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.best_store.right_bite.model.cart.CartItem}
 */
@Schema(name = "Item response")
public record CartItemResponseDto(
        @Schema(description = "id of the item in the cart")
        Long productId,
        @Schema(description = "product name")
        String productName,
        @Schema(description = "quantity")
        int quantity,
        @Schema(description = "price snapshot for one unit of the item")
        BigDecimal unitPriceSnapshot,
        @Schema(description = "total price for this item (quantity multiply unitPriceSnapshot)")
        BigDecimal totalPrice,
        @Schema(description = "item image url")
        String thumbnailUrl) implements Serializable {
}