package com.best_store.right_bite.dto.cart.request.removeFromCart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

@Schema(name = "remove item")
public record RemoveItemsRequestDto(
        @Schema(description = "ids of products for removing from the cart.")
        @NotNull
        @NotEmpty
        Set<@NotNull Long> idsToRemove
) implements Serializable {
}