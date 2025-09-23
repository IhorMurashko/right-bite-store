package com.best_store.right_bite.service.cart.price;

import com.best_store.right_bite.dto.catalog.product.ProductPriceDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.cart.UserCartNotFoundException;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.repository.catalog.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceUpdatableServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private PriceUpdatableServiceImpl priceUpdatableService;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    private Long userId;
    private Cart cart;
    private Set<CartItem> setWithTwoOldItems;
    private Set<CartItem> newCartItems;
    private List<ProductPriceDto> itemsWithActualPrice;
    private User user;

    @BeforeEach
    void setUp() {
        this.userId = 1L;
        this.user = new User();
        this.cart = new Cart(user);
        this.itemsWithActualPrice = List.of(
                new ProductPriceDto(1L, BigDecimal.valueOf(10)),
                new ProductPriceDto(2L, BigDecimal.valueOf(5)),
                new ProductPriceDto(3L, BigDecimal.valueOf(3))
        );
        this.newCartItems = Set.of(
                new CartItem(1L, "product1", 2, BigDecimal.valueOf(10),
                        BigDecimal.valueOf(20), "imageUrl", LocalDateTime.now(), cart),
                new CartItem(2L, "product2", 3, BigDecimal.valueOf(5),
                        BigDecimal.valueOf(15), "imageUrl", LocalDateTime.now(), cart),
                new CartItem(3L, "product3", 3, BigDecimal.valueOf(3),
                        BigDecimal.valueOf(9), "imageUrl", LocalDateTime.now(), cart)
        );
        this.setWithTwoOldItems = Set.of(
                new CartItem(1L, "product1", 2, BigDecimal.valueOf(8),
                        BigDecimal.valueOf(16), "imageUrl", LocalDateTime.now().minusMinutes(45), cart),
                new CartItem(2L, "product2", 3, BigDecimal.valueOf(5),
                        BigDecimal.valueOf(15), "imageUrl", LocalDateTime.now(), cart),
                new CartItem(3L, "product3", 3, BigDecimal.valueOf(4),
                        BigDecimal.valueOf(12), "imageUrl", LocalDateTime.now().minusMinutes(95), cart)
        );
    }

    @Test
    void shouldThrowUserCartNotFoundException_when_userCartByUserIdDoesNotExist() {
        doReturn(Optional.empty()).when(cartRepository).findCartByUserId(userId);

        UserCartNotFoundException exception = assertThrows(UserCartNotFoundException.class,
                () -> priceUpdatableService.refreshCartPrices(userId));

        assertNotNull(exception);
        assertEquals(String.format(ExceptionMessageProvider.USER_CART_WAS_NOT_FOUND, userId), exception.getMessage());
        verify(cartRepository).findCartByUserId(longArgumentCaptor.capture());
        assertEquals(userId, longArgumentCaptor.getValue());
        verify(cartRepository, times(1)).findCartByUserId(any(Long.class));
        verifyNoMoreInteractions(cartRepository);
        verifyNoInteractions(productRepository);
    }

    @Test
    void shouldReturnZeroValue_when_userCartIsEmpty() {
        Cart cartWithEmptyItems = new Cart(user);
        cartWithEmptyItems.setCartItems(Collections.emptySet());
        doReturn(Optional.of(cartWithEmptyItems)).when(cartRepository).findCartByUserId(anyLong());

        int modifyingCounter = priceUpdatableService.refreshCartPrices(userId);

        assertEquals(0, modifyingCounter);
        verify(cartRepository, times(1)).findCartByUserId(anyLong());
        verifyNoMoreInteractions(cartRepository);
        verifyNoInteractions(productRepository);
        verify(cartRepository).findCartByUserId(longArgumentCaptor.capture());
        assertEquals(userId, longArgumentCaptor.getValue());
    }

    @Test
    void shouldReturnModifyingCountValue_two_when_twoItemsWereModified() {
        doReturn(Optional.of(cart)).when(cartRepository).findCartByUserId(anyLong());
        cart.setCartItems(setWithTwoOldItems);
        doReturn(itemsWithActualPrice).when(productRepository).getProductPriceByProductIds(anySet());

        int modifyingCounter = priceUpdatableService.refreshCartPrices(userId);

        assertEquals(2, modifyingCounter);
        verify(cartRepository, times(1)).findCartByUserId(anyLong());
        verify(productRepository, times(1)).getProductPriceByProductIds(anySet());
        verify(cartRepository).findCartByUserId(longArgumentCaptor.capture());
        assertEquals(userId, longArgumentCaptor.getValue());
        verifyNoMoreInteractions(cartRepository, productRepository);
    }

    @Test
    void shouldReturnZeroValue_when_noItemsWereModified() {
        doReturn(Optional.of(cart)).when(cartRepository).findCartByUserId(anyLong());
        cart.setCartItems(newCartItems);
        doReturn(itemsWithActualPrice).when(productRepository).getProductPriceByProductIds(anySet());

        int modifyingCounter = priceUpdatableService.refreshCartPrices(userId);

        assertEquals(0, modifyingCounter);
        verify(cartRepository, times(1)).findCartByUserId(anyLong());
        verify(productRepository, times(1)).getProductPriceByProductIds(anySet());
        verify(cartRepository, times(0)).save(any(Cart.class));
        verify(cartRepository).findCartByUserId(longArgumentCaptor.capture());
        assertEquals(userId, longArgumentCaptor.getValue());
        verifyNoMoreInteractions(cartRepository, productRepository);
    }
}