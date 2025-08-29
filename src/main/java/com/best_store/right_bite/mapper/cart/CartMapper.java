package com.best_store.right_bite.mapper.cart;

import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.model.cart.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting {@link Cart} entities to DTOs.
 * Uses {@link CartItemMapper} to map nested cart items.
 * Ignores unmapped target properties.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CartItemMapper.class)
public interface CartMapper {
    /**
     * Converts a {@link Cart} entity to {@link CartResponseDto}.
     *
     * @param cart the cart entity to map
     * @return the mapped {@link CartResponseDto}
     */
    CartResponseDto toCartResponseDto(Cart cart);
}
