package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.dto.cart.response.CartItemResponseDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.db.InternalDataBaseConnectionException;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.mapper.cart.CartMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.service.cart.helper.CartProvider;
import com.best_store.right_bite.service.cart.price.PriceUpdatableService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;
    private Cart cart;
    private User user;
    private Long userId;
    private Set<CartItemResponseDto> cartItemResponseDto;
    private Set<CartItem> cartItems;
    private BigDecimal totalPrice;

    @BeforeEach
    void setUp() {
        this.user = new User();
        this.userId = 1L;
        this.user.setId(userId);
        this.cart = new Cart();
        this.cart.setUser(user);
        this.cartItems = Set.of(
                new CartItem(1L, "name1", 2,
                        BigDecimal.valueOf(15), BigDecimal.valueOf(30), "imag1", LocalDateTime.now(), cart),
                new CartItem(2L, "name2", 3,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(30), "imag2", LocalDateTime.now(), cart),
                new CartItem(3L, "name3", 1,
                        BigDecimal.valueOf(30), BigDecimal.valueOf(30), "imag3", LocalDateTime.now(), cart),
                new CartItem(4L, "name4", 1,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "imag4", LocalDateTime.now(), cart)
        );
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
    }

    @Nested
    class GetUserCartTest {

        @Test
        void shouldThrowUserNotFoundException_when_UserIdWasNotFound() {
            doThrow(new UserNotFoundException(String.format(ExceptionMessageProvider.USER_CART_WAS_NOT_FOUND, userId)))
                    .when(cartProvider).findCartByAuthUser(anyLong());

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> cartService.getUserCart(userId));

            assertEquals(String.format(ExceptionMessageProvider.USER_CART_WAS_NOT_FOUND, userId), exception.getMessage());
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verify(cartProvider, times(1)).findCartByAuthUser(anyLong());
            verifyNoMoreInteractions(cartProvider);
            verifyNoInteractions(cartMapper, cartRepository, priceUpdatableService);
        }

        @Test
        void shouldReturnEmptyUserCart_when_userCartIsEmpty() {
            cart.setCartItems(Collections.emptySet());
            doReturn(cart).when(cartProvider).findCartByAuthUser(anyLong());
            doReturn(0).when(priceUpdatableService).refreshCartPrices(anyLong());
            doReturn(new CartResponseDto(Collections.emptySet(), BigDecimal.ZERO)).when(cartMapper)
                    .toCartResponseDto(any(Cart.class));

            CartResponseDto cartResponseDto = cartService.getUserCart(userId);

            assertNotNull(cartResponseDto);
            assertTrue(cartResponseDto.cartItems().isEmpty());
            assertEquals(BigDecimal.ZERO, cartResponseDto.totalPrice());
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verify(cartProvider, times(1)).findCartByAuthUser(anyLong());
            verify(priceUpdatableService, times(1)).refreshCartPrices(anyLong());
            verify(cartMapper, times(1)).toCartResponseDto(any(Cart.class));
            verifyNoMoreInteractions(cartProvider, priceUpdatableService, cartMapper);
            verifyNoInteractions(cartRepository);
        }

        @Test
        void shouldReturnTheDtoOfCart_when_itemsPriceWasNotChanged() {
            cart.setCartItems(cartItems);
            doReturn(cart).when(cartProvider).findCartByAuthUser(anyLong());
            doReturn(0).when(priceUpdatableService).refreshCartPrices(anyLong());
            doReturn(new CartResponseDto(cartItemResponseDto, totalPrice)).when(cartMapper).toCartResponseDto(any(Cart.class));

            CartResponseDto cartResponseDto = cartService.getUserCart(userId);

            assertNotNull(cartResponseDto);
            assertEquals(cart.getCartItems().size(), cartResponseDto.cartItems().size());
            assertTrue(cartResponseDto.cartItems().containsAll(cartItemResponseDto));
            assertEquals(totalPrice, cartResponseDto.totalPrice());
            verify(priceUpdatableService).refreshCartPrices(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verify(cartProvider, times(1)).findCartByAuthUser(anyLong());
            verify(priceUpdatableService, times(1)).refreshCartPrices(anyLong());
            verify(cartMapper, times(1)).toCartResponseDto(any(Cart.class));
            verifyNoMoreInteractions(cartProvider, priceUpdatableService, cartMapper);
            verifyNoInteractions(cartRepository);
        }

        @Test
        void shouldThrowInternalDataBaseConnectionException_when_priceUpdatableServiceThrowsOptimisticLockException() {
            doReturn(cart).when(cartProvider).findCartByAuthUser(anyLong());
            doThrow(OptimisticLockException.class).when(priceUpdatableService).refreshCartPrices(anyLong());

            InternalDataBaseConnectionException exception = assertThrows(InternalDataBaseConnectionException.class,
                    () -> cartService.getUserCart(userId));

            assertNotNull(exception.getMessage());
            assertEquals(String.format(ExceptionMessageProvider.OPTIMISTIC_LOCKING_EXCEPTION, userId), exception.getMessage());
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verify(cartProvider, times(1)).findCartByAuthUser(anyLong());
            verify(priceUpdatableService, times(3)).refreshCartPrices(anyLong());
            verifyNoMoreInteractions(cartProvider, priceUpdatableService);
            verifyNoInteractions(cartMapper, cartRepository);
        }

        @Test
        void shouldReturnTheDtoOfCartWithUpdatedPrice_when_itemsPriceWasUpdatedAfterTwoOptimisticLockExceptionsThrown() {
            cart.setCartItems(cartItems);
            doReturn(cart).when(cartProvider).findCartByAuthUser(anyLong());
            BigDecimal newTotalPrice = totalPrice.add(BigDecimal.valueOf(10));
            when(priceUpdatableService.refreshCartPrices(anyLong()))
                    .thenThrow(OptimisticLockException.class)
                    .thenThrow(OptimisticLockException.class)
                    .thenReturn(2);
            doReturn(cart).when(cartRepository).save(any(Cart.class));
            doReturn(new CartResponseDto(cartItemResponseDto, newTotalPrice)).when(cartMapper).toCartResponseDto(any(Cart.class));

            CartResponseDto cartResponseDto = cartService.getUserCart(userId);

            assertNotNull(cartResponseDto);
            assertEquals(cart.getCartItems().size(), cartResponseDto.cartItems().size());
            assertEquals(newTotalPrice, cartResponseDto.totalPrice());
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verify(cartProvider, times(1)).findCartByAuthUser(anyLong());
            verify(priceUpdatableService, times(3)).refreshCartPrices(anyLong());
            verify(cartRepository, times(1)).save(any(Cart.class));
            verify(cartMapper, times(1)).toCartResponseDto(any(Cart.class));
            verifyNoMoreInteractions(cartProvider, priceUpdatableService, cartRepository, cartMapper);
        }

    }


}