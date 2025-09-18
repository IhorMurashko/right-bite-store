package com.best_store.right_bite.service.cart.application;

import com.best_store.right_bite.dto.cart.request.addToCart.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.removeFromCart.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.model.cart.Cart;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;

/**
 * Facade interface for managing shopping cart operations for authenticated users.
 * Provides methods for retrieving, adding, removing, and clearing cart items.
 */
public interface CartService {

    /**
     * Retrieves the cart of the authenticated user, creating one if it does not exist.
     *
     * @param authentication current authenticated user
     * @return the user's cart
     */
    Cart findCartByAuthUser(@NonNull Authentication authentication);

    /**
     * Retrieves the cart of the authenticated user as a DTO,
     * automatically updating prices if product prices have changed.
     *
     * @param authentication current authenticated user
     * @return cart data as {@link CartResponseDto}
     */
    CartResponseDto getUserCart(@NonNull Authentication authentication);

    /**
     * Adds or updates items in the authenticated user's cart.
     *
     * @param addCartItems   DTO with items to add or update
     * @param authentication current authenticated user
     * @return updated cart as {@link CartResponseDto}
     */
    CartResponseDto addItems(@NonNull @Valid AddCartRequestDto addCartItems, @NonNull Authentication authentication);

    /**
     * Removes items from the authenticated user's cart by product IDs.
     *
     * @param removeItems    DTO containing IDs of products to remove
     * @param authentication current authenticated user
     * @return updated cart as {@link CartResponseDto}
     */
    CartResponseDto removeItems(@NonNull @Valid RemoveItemsRequestDto removeItems, @NonNull Authentication authentication);

    /**
     * Clears all items from the authenticated user's cart.
     *
     * @param authentication current authenticated user
     */
    void clear(@NonNull Authentication authentication);
}
