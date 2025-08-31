package com.best_store.right_bite.dto.adminPanel.order;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.model.order.OrderDeliveryDetails;
import com.best_store.right_bite.model.order.OrderItem;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record OrderDTO(
        Long id,
        DefaultUserInfoResponseDto user,
        OrderStatus orderStatus,
        Set<OrderItem> items,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        OrderDeliveryDetails orderDeliveryDetails)
{

}
