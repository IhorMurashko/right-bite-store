package com.best_store.right_bite.service.order.domain;


import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.model.order.Order;
import com.best_store.right_bite.repository.order.OrderRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public BigDecimal totalPriceOrders() {
        log.info("total price orders");
        BigDecimal totalPrice = orderRepository.getTotalPrice();
        if (totalPrice == null) totalPrice = BigDecimal.ZERO;
        return totalPrice;
    }

    @Override
    public Long totalCountOrders() {
        log.info("total count orders");
        return orderRepository.count();
    }

    @Override
    public Optional<Order> findById(@NotNull Long id) {
        log.debug("finding order by id: {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAllByUserId(@NotNull Long userId) {
        log.debug("finding all orders by user id: {}", userId);
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public Order save(@NotNull Order order) {
        log.debug("saving order: {}", order);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByStatus(@NotNull OrderStatus status) {
        log.debug("finding all orders by status: {}", status);
        return orderRepository.findOrderByOrderStatus(status);
    }

    @Override
    public List<Order> findAll() {
        log.debug("finding all orders");
        return orderRepository.findAll();
    }

    @Override
    public void deleteById(@NotNull Long id) {
        log.warn("Deleting order by id: {}", id);
        orderRepository.deleteById(id);
    }
}