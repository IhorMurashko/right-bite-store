package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.repository.cart.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * The default implementation of the {@link CartService} interface.
 * <p>
 * This class handles the business logic for cart operations by coordinating with
 * the {@link CartRepository}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    /**
     * {@inheritDoc}
     * <p>
     * This operation is executed within a read-committed transaction.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Override
    public Optional<Cart> getCartByUserId(@NonNull Long userId) {
        log.debug("getting user cart by user id: {}", userId);
        return cartRepository.findCartByUserId(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Cart save(@NonNull Cart cart) {
        log.debug("cart was saved for user {}", cart.getUser().getId());
        return cartRepository.save(cart);
    }
}
