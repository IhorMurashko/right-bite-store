package com.best_store.right_bite.dto.order.request;

import com.best_store.right_bite.dto.order.assing.OrderRequestAssigner;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Set;

import static com.best_store.right_bite.constant.constraint.order.OrderFieldConstraint.*;

/**
 * DTO for {@link com.best_store.right_bite.model.order.Order}
 */
public record OrderDto(
        @ArraySchema(arraySchema = @Schema(description = "Order items"))
        @NotNull
        @Size(min = MIN_ITEMS_QUANTITY, max = MAX_ITEMS_QUANTITY, message = ITEMS_QUANTITY_EXCEPTION_MESSAGE)
        Set<OrderItemDto> items,
        @Schema(description = "Delivery details")
        @NotNull
        OrderDeliveryDetailsDto deliveryDetails) implements OrderRequestAssigner, Serializable {
}