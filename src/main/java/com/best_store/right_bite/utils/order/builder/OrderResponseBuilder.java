package com.best_store.right_bite.utils.order.builder;

import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.dto.order.response.OrdersPageableDto;
import com.best_store.right_bite.model.order.Order;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;

@UtilityClass
public class OrderResponseBuilder {
    public OrdersPageableDto buildOrdersPageable(List<OrderResponseDto> orderResponseDtos, Page<Order> orderPage) {
        return new OrdersPageableDto(
                orderResponseDtos,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast());
    }
}
