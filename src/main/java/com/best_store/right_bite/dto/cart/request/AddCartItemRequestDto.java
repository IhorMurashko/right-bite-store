package com.best_store.right_bite.dto.cart.request;

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
public record AddCartItemRequestDto(
        @NonNull
        Long productId,
        @NonNull
        @NotBlank
        String productName,
        @Min(1)
        int quantity,
        @NonNull
        BigDecimal unitPriceSnapshot,
        @NonNull
        @NotBlank
        String thumbnailUrl) implements Serializable {
}