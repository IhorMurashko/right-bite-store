package com.best_store.right_bite.dto.order.response;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderItemDto;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.best_store.right_bite.model.order.Order}
 */
public record OrderResponseDto(
        @Schema(description = "Order id", example = "42")
        Long id,
        @Schema(description = "User basic info")
        DefaultUserInfoResponseDto user,
        @Schema(description = "Current order status")
        OrderStatus orderStatus,
        @ArraySchema(arraySchema = @Schema(description = "Order items"))
        Set<OrderItemDto> items,
        @Schema(description = "Total price of the order", example = "59.97")
        BigDecimal totalPrice,
        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,
        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt,
        @Schema(description = "Delivery details")
        OrderDeliveryDetailsDto orderDeliveryDetails) implements Serializable {
}