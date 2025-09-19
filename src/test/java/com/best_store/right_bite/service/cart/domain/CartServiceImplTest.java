package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.dto.cart.response.CartItemResponseDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.mapper.cart.CartMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.service.cart.helper.CartProvider;
import com.best_store.right_bite.service.cart.price.PriceUpdatableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private PriceUpdatableService priceUpdatableService;
    @Mock
    private CartProvider cartProvider;
    @InjectMocks
    private CartServiceImpl cartService;
    private Cart cart;
    private User user;
    private Long userId;
    private Set<CartItemResponseDto> cartItemResponseDto;
    private BigDecimal totalPrice;
    private CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        this.user = new User();
        this.userId = 1L;
        this.user.setId(userId);
        this.cart = new Cart();
        this.cart.setUser(user);
        this.cartItemResponseDto = Set.of(
                new CartItemResponseDto(1L, "name1", 2,
                        BigDecimal.valueOf(15), BigDecimal.valueOf(30), "imag1"),
                new CartItemResponseDto(2L, "name2", 3,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(30), "imag2"),
                new CartItemResponseDto(3L, "name3", 1,
                        BigDecimal.valueOf(30), BigDecimal.valueOf(30), "imag3"),
                new CartItemResponseDto(4L, "name4", 1,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "imag4")
                );
        this.totalPrice = BigDecimal.valueOf(100);
        this.cartResponseDto = new CartResponseDto(cartItemResponseDto, totalPrice);
    }
}