package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.dto.cart.request.addToCart.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.removeFromCart.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;


public interface CartService {

    CartResponseDto getUserCart(@NonNull Long userId);

    CartResponseDto addItems(@NonNull @Valid AddCartRequestDto addCartItems, @NonNull Long userId);

    CartResponseDto removeItems(@NonNull @Valid RemoveItemsRequestDto removeItems, @NonNull Long userId);

    void clear(@NonNull Long userId);
}
