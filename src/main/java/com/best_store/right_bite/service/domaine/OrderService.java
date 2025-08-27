package com.best_store.right_bite.service.domaine;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.model.order.Order;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> findById(@NotNull Long id);

    List<Order> findAllByUserId(@NotNull Long userId);

    Order save(@NotNull Order order);

    List<Order> findByStatus(@NotNull OrderStatus status);

    List<Order> findAll();

    void deleteById(@NotNull Long id);
}
