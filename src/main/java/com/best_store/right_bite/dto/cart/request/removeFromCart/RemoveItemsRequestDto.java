package com.best_store.right_bite.dto.cart.request.removeFromCart;

import com.best_store.right_bite.exception.messageProvider.CartExceptionMP;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.Set;

@Schema(name = "remove item")
public record RemoveItemsRequestDto(
        @Schema(description = "ids of products for removing from the cart.")
        @NotNull(message = CartExceptionMP.REMOVING_LIST_IS_NULL)
        @NotEmpty(message = CartExceptionMP.REMOVING_LIST_IS_EMPTY)
        Set<@NotNull @Positive(message = CartExceptionMP.POSITIVE_REMOVING_ID) Long> idsToRemove
) implements Serializable {
}