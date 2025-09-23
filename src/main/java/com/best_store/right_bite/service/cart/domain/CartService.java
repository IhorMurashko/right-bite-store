package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.dto.cart.request.addToCart.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.removeFromCart.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Service interface for managing shopping cart operations.
 *
 * <p>Provides methods for retrieving, modifying, and managing user shopping carts.
 * All operations are user-specific and require valid user identification.</p>
 *
 * @author Ihor Murashko
 */
public interface CartService {

    /**
     * Retrieves the current shopping cart for the specified user.
     *
     * @param userId the unique identifier of the user whose cart to retrieve
     * @return {@link CartResponseDto} containing the user's cart items and total price
     * @throws IllegalArgumentException if userId is null
     */
    CartResponseDto getUserCart(@NotNull Long userId);

    /**
     * Adds multiple items to the user's shopping cart.
     *
     * <p>If an item with the same product ID already exists in the cart,
     * it will be updated with the new quantity and price information.</p>
     *
     * @param addCartItems the request containing items to add to the cart
     * @param userId       the unique identifier of the user whose cart to modify
     * @return {@link CartResponseDto} containing the updated cart state
     * @throws IllegalArgumentException                        if addCartItems or userId is null
     * @throws jakarta.validation.ConstraintViolationException if addCartItems fails validation
     */
    CartResponseDto addItems(@NotNull @Valid AddCartRequestDto addCartItems, @NotNull Long userId);

    /**
     * Removes specified items from the user's shopping cart.
     *
     * <p>Items are identified by their product IDs. Non-existing items are silently ignored.</p>
     *
     * @param removeItems the request containing product IDs to remove from the cart
     * @param userId      the unique identifier of the user whose cart to modify
     * @return {@link CartResponseDto} containing the updated cart state after removal
     * @throws IllegalArgumentException                        if removeItems or userId is null
     * @throws jakarta.validation.ConstraintViolationException if removeItems fails validation
     */
    CartResponseDto removeItems(@NotNull @Valid RemoveItemsRequestDto removeItems, @NotNull Long userId);

    /**
     * Removes all items from the user's shopping cart.
     *
     * <p>This operation clears the entire cart and resets the total price to zero.</p>
     *
     * @param userId the unique identifier of the user whose cart to clear
     * @throws IllegalArgumentException if userId is null
     */
    void clear(@NotNull Long userId);
}
