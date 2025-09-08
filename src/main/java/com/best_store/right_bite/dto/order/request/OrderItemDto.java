package com.best_store.right_bite.dto.order.request;

import com.best_store.right_bite.constant.constraint.order.OrderFieldConstraint;
import com.best_store.right_bite.dto.order.assing.OrderItemsRequestAssigner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.best_store.right_bite.model.order.OrderItem}
 */
public record OrderItemDto(
        @Schema(description = "Product identifier", example = "1001")
        @NotNull
        Long productId,
        @Schema(description = "Quantity of the product", example = "2")
        @Min(value = OrderFieldConstraint.MIN_ITEMS_QUANTITY, message = OrderFieldConstraint.ITEMS_QUANTITY_EXCEPTION_MESSAGE)
        @Max(value = OrderFieldConstraint.MAX_ITEMS_QUANTITY, message = OrderFieldConstraint.ITEMS_QUANTITY_EXCEPTION_MESSAGE)
        int quantity,
        @Schema(description = "Product name at time of ordering")
        @NotNull
        String productName,
        @Schema(description = "Unit price snapshot", example = "19.99")
        @NotNull
        BigDecimal priceSnapshot) implements OrderItemsRequestAssigner, Serializable {
}