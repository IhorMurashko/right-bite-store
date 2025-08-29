package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.model.cart.Cart;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Defines the contract for business operations related to the user's shopping cart.
 * <p>
 * This service provides the core functionalities for retrieving and persisting {@link Cart} entities.
 */
public interface CartService {

    /**
     * Retrieves the shopping cart for a specific user.
     *
     * @param userId the unique identifier of the user whose cart is to be retrieved. Must not be {@code null}.
     * @return an {@link Optional} containing the user's {@link Cart} if found,
     * or an empty {@link Optional} if the user does not have a cart yet.
     */
    Optional<Cart> getCartByUserId(@NonNull Long userId);

    /**
     * Saves or updates a shopping cart in the database.
     * <p>
     * If the cart is new (has a null ID), it will be created. If it already exists,
     * its state will be updated.
     *
     * @param cart the {@link Cart} entity to save. Must not be {@code null}.
     * @return the saved cart instance, which may include updates from the persistence layer
     * (e.g., a generated ID or updated timestamps).
     */
    Cart save(@NonNull Cart cart);
}
