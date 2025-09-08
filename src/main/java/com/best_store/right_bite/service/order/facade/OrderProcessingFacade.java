package com.best_store.right_bite.service.order.facade;

import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderDto;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

public interface OrderProcessingFacade {
    OrderResponseDto processOrder(@NotNull @Valid OrderDto orderDto, @Nullable Authentication authentication);

    OrderResponseDto processOrder(@NotNull Long UserId, @NotNull @Valid OrderDeliveryDetailsDto orderDeliveryDetailsDto);
}
