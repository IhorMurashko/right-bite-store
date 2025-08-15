package com.best_store.right_bite.dto.cart.request.remove;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

public record RemoveItemsRequestDto(
        @NotNull
        @NotEmpty
        Set<@NotNull Long> idsToRemove
) implements Serializable {
}