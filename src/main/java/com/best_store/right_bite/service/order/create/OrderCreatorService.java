package com.best_store.right_bite.service.order.create;

import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderDto;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.model.user.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

public interface OrderCreatorService {
    OrderResponseDto createGuestOrder(@NotNull @Valid OrderDto orderDto, @Nullable User user);

    OrderResponseDto createOrderFromCart(@NotNull User user, @NotNull @Valid OrderDeliveryDetailsDto orderDeliveryDetailsDto);
//todo: new service for order update
//    OrderResponseDto updateOrder(OrderDto orderDto);
}
