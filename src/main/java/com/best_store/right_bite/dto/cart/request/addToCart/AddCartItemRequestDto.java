package com.best_store.right_bite.dto.cart.request.addToCart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object representing a request to add an item to the cart.
 *
 * <p>Contains product details including ID, name, quantity, snapshot price, and thumbnail URL.</p>
 *
 * @see com.best_store.right_bite.model.cart.CartItem
 */
@Schema(name = "product info")
public record AddCartItemRequestDto(
        @Schema(description = "id of the product", nullable = false)
        @NonNull
        Long productId,
        @Schema(description = "name of the product", nullable = false)
        @NonNull
        @NotBlank
        String productName,
        @Schema(description = "quantity of product", nullable = false, minimum = "1")
        @Min(1)
        int quantity,
        @Schema(description = "price snapshot of the item", nullable = false)
        @NonNull
        BigDecimal unitPriceSnapshot,
        @Schema(description = "product image ulr", nullable = false)
        @NonNull
        @NotBlank
        String thumbnailUrl) implements Serializable {
}