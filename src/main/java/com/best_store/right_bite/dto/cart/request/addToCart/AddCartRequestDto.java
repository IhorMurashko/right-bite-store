package com.best_store.right_bite.dto.cart.request.addToCart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * Data Transfer Object representing a request to add multiple items to the cart.
 *
 * <p>Contains a set of {@link AddCartItemRequestDto} representing the items to add.</p>
 *
 * @see com.best_store.right_bite.model.cart.Cart
 */
@Schema(name = "list of products for adding to a cart")
public record AddCartRequestDto(
        @Schema(description = "list of uniq products", nullable = false)
        @NotNull
        @NotEmpty
        Set<AddCartItemRequestDto> cartItems) implements Serializable {
}