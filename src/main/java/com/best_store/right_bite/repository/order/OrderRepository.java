package com.best_store.right_bite.repository.order;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByUserId(long userId, Pageable pageable);

    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
