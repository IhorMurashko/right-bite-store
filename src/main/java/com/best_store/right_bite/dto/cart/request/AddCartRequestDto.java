package com.best_store.right_bite.dto.cart.request;

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
public record AddCartRequestDto(
        @NotNull
        @NotEmpty
        Set<AddCartItemRequestDto> cartItems) implements Serializable {
}