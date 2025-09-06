package com.best_store.right_bite.service.order.finder;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.dto.order.response.OrdersPageableDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

public interface OrderFinderService {
    OrderResponseDto findUserOrderById(@NotNull Long orderId, @Nullable Long userId);

    OrdersPageableDto findAllUserOrders(Long userId, Pageable pageable);

    OrdersPageableDto findAllOrders(Pageable pageable);

    OrdersPageableDto findOrdersByStatus(OrderStatus status, Pageable pageable);
}
