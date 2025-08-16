package com.best_store.right_bite.dto.cart.request.remove;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;
@Schema(name = "")
public record RemoveItemsRequestDto(
        @Schema(description = "ids of products for removing from the cart.", nullable = false)
        @NotNull
        @NotEmpty
        Set<@NotNull Long> idsToRemove
) implements Serializable {
}