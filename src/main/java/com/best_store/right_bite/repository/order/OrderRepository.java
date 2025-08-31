package com.best_store.right_bite.repository.order;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long id);

    Optional<Order> findOrderByUserId(Long userId);

    List<Order> findAllByUserId(Long userId);

    List<Order> findOrderByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    BigDecimal getTotalPrice();

}
