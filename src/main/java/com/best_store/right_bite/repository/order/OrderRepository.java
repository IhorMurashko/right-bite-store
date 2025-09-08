package com.best_store.right_bite.repository.order;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    List<Order> findAllByUserId(Long userId);

    List<Order> findOrderByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    BigDecimal getTotalPrice();

    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
