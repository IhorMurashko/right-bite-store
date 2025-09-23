package com.best_store.right_bite.repository.cart;

import com.best_store.right_bite.model.cart.Cart;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Cart> findCartByUserId(Long userId);
}