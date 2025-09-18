package com.best_store.right_bite.service.cart.helper;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartProviderTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CartProvider cartProvider;
    private User user;
    private Long userId;
    private Cart savedNewCart;

    @BeforeEach
    void setUp() {
        this.user = new User();
        this.userId = 1L;
        this.user.setId(userId);
        this.savedNewCart = new Cart();
        this.savedNewCart.setUser(user);
    }


    @Test
    void doThrowUserNotFoundException_when_userWithIdDoesNotExist() {
        doReturn(Optional.empty()).when(userRepository).findById(any());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> cartProvider.findCartByAuthUser(userId));

        assertNotNull(exception);
        assertEquals(String.format(ExceptionMessageProvider.USER_ID_NOT_FOUND, userId), exception.getMessage());
        verifyNoInteractions(cartRepository);
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    void getNewCart_when_cartNotExistForUser() {
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(Optional.empty()).when(cartRepository).findCartByUserId(any());
        doReturn(savedNewCart).when(cartRepository).save(any(Cart.class));

        Cart cartByAuthUser = cartProvider.findCartByAuthUser(userId);

        assertNotNull(cartByAuthUser);
        assertTrue(cartByAuthUser.getCartItems().isEmpty());
        assertEquals(user, cartByAuthUser.getUser());
        assertEquals(BigDecimal.ZERO, cartByAuthUser.getTotalPrice());
        verify(userRepository, times(1)).findById(any());
        verify(cartRepository, times(1)).findCartByUserId(any());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }


    @Test
    void getExistingEmptyCart_when_cartExistsForUser() {
        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(Optional.of(savedNewCart)).when(cartRepository).findCartByUserId(any());

        Cart cartByAuthUser = cartProvider.findCartByAuthUser(userId);

        assertNotNull(cartByAuthUser);
        assertTrue(cartByAuthUser.getCartItems().isEmpty());
        assertEquals(user, cartByAuthUser.getUser());
        assertEquals(BigDecimal.ZERO, cartByAuthUser.getTotalPrice());
        verify(userRepository, times(1)).findById(any());
        verify(cartRepository, times(1)).findCartByUserId(any());
        verifyNoMoreInteractions(cartRepository, userRepository);
    }

    @Test
    void getExistingCartWithItems_when_cartExistsForUser() {
        savedNewCart.addItem(new CartItem(1L, "name", 2, BigDecimal.valueOf(5), BigDecimal.valueOf(10),
                "image", LocalDateTime.now(), savedNewCart));
        savedNewCart.addItem(new CartItem(2L, "name", 2, BigDecimal.valueOf(5), BigDecimal.valueOf(10),
                "image", LocalDateTime.now(), savedNewCart));
        savedNewCart.addItem(new CartItem(3L, "name", 2, BigDecimal.valueOf(5), BigDecimal.valueOf(10),
                "image", LocalDateTime.now(), savedNewCart));
        savedNewCart.setTotalPrice(BigDecimal.valueOf(30));

        doReturn(Optional.of(user)).when(userRepository).findById(any());
        doReturn(Optional.of(savedNewCart)).when(cartRepository).findCartByUserId(any());

        Cart cartByAuthUser = cartProvider.findCartByAuthUser(userId);

        assertNotNull(cartByAuthUser);
        assertEquals(3, cartByAuthUser.getCartItems().size());
        assertEquals(user, cartByAuthUser.getUser());
        assertEquals(BigDecimal.valueOf(30), cartByAuthUser.getTotalPrice());
        verify(userRepository, times(1)).findById(any());
        verify(cartRepository, times(1)).findCartByUserId(any());
        verifyNoMoreInteractions(cartRepository, userRepository);
    }
}