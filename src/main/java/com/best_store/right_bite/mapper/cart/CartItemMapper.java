package com.best_store.right_bite.mapper.cart;

import com.best_store.right_bite.dto.cart.response.CartItemResponseDto;
import com.best_store.right_bite.model.cart.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartItemMapper {

//    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);
    Set<CartItemResponseDto> toCartItemResponseDtos(Set<CartItem> cartItems);

}