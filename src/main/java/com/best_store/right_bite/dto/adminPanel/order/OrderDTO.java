package com.best_store.right_bite.dto.adminPanel.order;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderItemDto;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.model.order.OrderDeliveryDetails;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record OrderDTO(
        Long id,
        DefaultUserInfoResponseDto user,
        OrderStatus orderStatus,
        Set<OrderItemDto> items,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        OrderDeliveryDetailsDto orderDeliveryDetails) {
}