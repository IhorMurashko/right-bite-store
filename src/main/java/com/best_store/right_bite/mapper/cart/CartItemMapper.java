package com.best_store.right_bite.mapper.cart;

import com.best_store.right_bite.dto.cart.response.CartItemResponseDto;
import com.best_store.right_bite.model.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

/**
 * Mapper interface for converting {@link CartItem} entities to their corresponding DTOs.
 * <p>
 * Uses MapStruct to generate implementation code at compile time.
 * Ignores unmapped target properties by default.
 * </p>
 *
 * <p>
 * This mapper is used to transform a collection of {@link CartItem} entities into a collection of
 * {@link CartItemResponseDto} objects for data transfer, typically when returning cart data to the client.
 * </p>
 *
 * @see CartItem
 * @see CartItemResponseDto
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartItemMapper {

    /**
     * Converts a set of {@link CartItem} entities into a set of {@link CartItemResponseDto} DTOs.
     *
     * @param cartItems set of cart item entities to convert
     * @return set of corresponding DTOs
     */
    Set<CartItemResponseDto> toCartItemResponseDtos(Set<CartItem> cartItems);

}