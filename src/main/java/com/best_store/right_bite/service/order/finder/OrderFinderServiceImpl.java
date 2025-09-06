package com.best_store.right_bite.service.order.finder;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.dto.order.response.OrdersPageableDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.order.access.OrderAccessDeniedException;
import com.best_store.right_bite.exception.order.order.OrderNotFoundException;
import com.best_store.right_bite.mapper.order.response.OrderResponseMapper;
import com.best_store.right_bite.model.order.Order;
import com.best_store.right_bite.repository.order.OrderRepository;
import com.best_store.right_bite.utils.order.builder.OrderResponseBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderFinderServiceImpl implements OrderFinderService {

    private final OrderRepository orderRepository;
    private final OrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto findUserOrderById(@NotNull Long orderId, @Nullable Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        String.format(ExceptionMessageProvider.ORDER_ID_NOT_FOUND, orderId)));
        validateOrderAccess(order, userId);
        log.info("Order {} found for user {}", orderId, userId);
        return orderResponseMapper.toDTO(order);
    }

    @Override
    public OrdersPageableDto findAllUserOrders(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        List<OrderResponseDto> orderResponseDtos = getOrderResponseDtos(orders);
        log.info("Finding orders for user {} with pagination {}", userId, pageable);
        return OrderResponseBuilder.buildOrdersPageable(orderResponseDtos, orders);
    }

    @Override
    public OrdersPageableDto findAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderResponseDto> orderResponseDtos = getOrderResponseDtos(orders);
        log.info("Finding orders with total elements: {} and with pagination {}", orders.getTotalElements(), pageable);
        return OrderResponseBuilder.buildOrdersPageable(orderResponseDtos, orders);
    }

    @Override
    public OrdersPageableDto findOrdersByStatus(OrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByOrderStatus(status, pageable);
        List<OrderResponseDto> orderResponseDtos = getOrderResponseDtos(orders);
        log.info("Finding orders with status {} with pagination {}", status, pageable);
        return OrderResponseBuilder.buildOrdersPageable(orderResponseDtos, orders);
    }

    private List<OrderResponseDto> getOrderResponseDtos(Page<Order> orders) {
        return orderResponseMapper.toDTOList(orders.getContent());
    }

    private void validateOrderAccess(Order order, Long userId) {
        if (order.getUser() == null) {
            return;
        }
        if (!order.getUser().getId().equals(userId)) {
            throw new OrderAccessDeniedException(String.format(
                    ExceptionMessageProvider.ORDER_ACCESS_DENIED, order.getId()
            ));
        }
    }
}
