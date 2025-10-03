package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.dto.cart.request.addToCart.AddCartItemRequestDto;
import com.best_store.right_bite.dto.cart.request.addToCart.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.removeFromCart.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartItemResponseDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.exception.exceptions.cart.UserCartNotFoundException;
import com.best_store.right_bite.exception.exceptions.db.InternalDataBaseConnectionException;
import com.best_store.right_bite.exception.messageProvider.BaseExceptionMessageProvider;
import com.best_store.right_bite.exception.messageProvider.CartExceptionMP;
import com.best_store.right_bite.mapper.cart.CartMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.service.cart.helper.CartProvider;
import com.best_store.right_bite.service.cart.price.PriceUpdatableService;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    private Set<AddCartItemRequestDto> addCartItemRequestDtos;
    private RemoveItemsRequestDto removeItemsRequestDto;
    private static Validator validator;


    @BeforeEach
    void setUp() {
        this.user = new User();
        this.userId = 1L;
        this.user.setId(userId);
        this.cart = new Cart();
        this.cart.setUser(user);
        this.cartItems = new HashSet<>() {{
            add(new CartItem(1L, "name1", 2,
                    BigDecimal.valueOf(15), BigDecimal.valueOf(30), "imag1", LocalDateTime.now(), cart));
            add(new CartItem(2L, "name2", 3,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(30), "imag2", LocalDateTime.now(), cart));
            add(new CartItem(3L, "name3", 1,
                    BigDecimal.valueOf(30), BigDecimal.valueOf(30), "imag3", LocalDateTime.now(), cart));
            add(new CartItem(4L, "name4", 1,
                    BigDecimal.valueOf(10), BigDecimal.valueOf(10), "imag4", LocalDateTime.now(), cart));
        }};

        this.cartItemResponseDto = new HashSet<>() {
            {
                add(new CartItemResponseDto(1L, "name1", 2,
                        BigDecimal.valueOf(15), BigDecimal.valueOf(30), "imag1"));
                add(new CartItemResponseDto(2L, "name2", 3,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(30), "imag2"));
                add(new CartItemResponseDto(3L, "name3", 1,
                        BigDecimal.valueOf(30), BigDecimal.valueOf(30), "imag3"));
                add(new CartItemResponseDto(4L, "name4", 1,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "imag4"));
            }
        };
        this.totalPrice = BigDecimal.valueOf(100);
        this.addCartItemRequestDtos = new HashSet<>() {
            {
                add(new AddCartItemRequestDto(1L, "product1", 3, BigDecimal.valueOf(5), "picture"));
                add(new AddCartItemRequestDto(2L, "product2", 5, BigDecimal.valueOf(4), "picture"));
                add(new AddCartItemRequestDto(3L, "product3", 1, BigDecimal.valueOf(2), "picture"));
                add(new AddCartItemRequestDto(4L, "product4", 1, BigDecimal.valueOf(10), "picture")
                );
            }
        };
        this.removeItemsRequestDto = new RemoveItemsRequestDto(Set.of(1L, 2L));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Nested
    class GetUserCartTest {

        @Test
        void shouldThrowUserCartNotFoundException_when_UserIdWasNotFound() {
            doThrow(new UserCartNotFoundException(String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId)))
                    .when(cartProvider).findCartByAuthUser(anyLong());

            UserCartNotFoundException exception = assertThrows(UserCartNotFoundException.class, () -> cartService.getUserCart(userId));

            assertEquals(String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId), exception.getMessage());
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
            assertEquals(String.format(BaseExceptionMessageProvider.OPTIMISTIC_LOCKING_EXCEPTION, userId), exception.getMessage());
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

    @Nested
    class AddItemToCartTest {

        @Test
        void shouldThrowConstraintViolationException_when_cartItemsIsNull() {
            AddCartRequestDto request = new AddCartRequestDto(null);

            Set<ConstraintViolation<AddCartRequestDto>> violations = validator.validate(request);

            assertEquals(2, violations.size());
            Set<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            assertTrue(messages.contains(CartExceptionMP.ITEMS_NULL));
            assertTrue(messages.contains(CartExceptionMP.ITEMS_EMPTY));
            verifyNoInteractions(cartProvider, cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldThrowConstraintViolationException_when_cartItemsIsEmpty() {
            AddCartRequestDto request = new AddCartRequestDto(new HashSet<>());

            Set<ConstraintViolation<AddCartRequestDto>> violations = validator.validate(request);

            assertEquals(1, violations.size());
            Set<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            assertTrue(messages.contains(CartExceptionMP.ITEMS_EMPTY));
            verifyNoInteractions(cartProvider, cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldThrowUserCartNotFoundException_when_cartNotFound() {
            doThrow(new UserCartNotFoundException(
                    String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId)))
                    .when(cartProvider).findCartByAuthUser(anyLong());

            UserCartNotFoundException ex = assertThrows(UserCartNotFoundException.class,
                    () -> cartService.addItems(new AddCartRequestDto(addCartItemRequestDtos), userId));

            assertEquals(String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId), ex.getMessage());
            verify(cartProvider).findCartByAuthUser(userId);
            verifyNoInteractions(cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldAddNewItemsToEmptyCart() {
            cart.setCartItems(new HashSet<>());
            doReturn(cart).when(cartProvider).findCartByAuthUser(userId);
            doReturn(cart).when(cartRepository).save(any(Cart.class));
            doReturn(new CartResponseDto(cartItemResponseDto, totalPrice)).when(cartMapper).toCartResponseDto(any());

            CartResponseDto response = cartService.addItems(new AddCartRequestDto(addCartItemRequestDtos), userId);

            assertNotNull(response);
            assertEquals(addCartItemRequestDtos.size(), response.cartItems().size());
            assertEquals(totalPrice, response.totalPrice());
            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartRepository).save(cart);
            verify(cartMapper).toCartResponseDto(cart);
        }

        @Test
        void shouldUpdateExistingItemsAndAddNewOnes() {
            cart.setCartItems(new HashSet<>() {{
                add(new CartItem(1L, "existing1", 2, BigDecimal.valueOf(10), BigDecimal.valueOf(20), "img1", LocalDateTime.now(), cart));
                add(new CartItem(2L, "existing2", 1, BigDecimal.valueOf(5), BigDecimal.valueOf(5), "img2", LocalDateTime.now(), cart));
            }});
            doReturn(cart).when(cartProvider).findCartByAuthUser(userId);
            doReturn(cart).when(cartRepository).save(any());
            doReturn(new CartResponseDto(cartItemResponseDto, totalPrice)).when(cartMapper).toCartResponseDto(any());

            Set<AddCartItemRequestDto> dtos = new HashSet<>() {{
                add(new AddCartItemRequestDto(1L, "existing1", 5, BigDecimal.valueOf(12), "img1new")); // обновление
                add(new AddCartItemRequestDto(3L, "new1", 2, BigDecimal.valueOf(7), "img3"));         // новый
                add(new AddCartItemRequestDto(4L, "new2", 1, BigDecimal.valueOf(3), "img4"));         // новый
            }};

            CartResponseDto response = cartService.addItems(new AddCartRequestDto(dtos), userId);

            assertNotNull(response);
            assertEquals(4, response.cartItems().size());
            CartItem updated = cart.getCartItems().stream()
                    .filter(i -> i.getProductId().equals(1L))
                    .findFirst().orElseThrow();
            assertEquals(5, updated.getQuantity());
            assertEquals(BigDecimal.valueOf(12), updated.getUnitPriceSnapshot());
            assertTrue(cart.getCartItems().stream().anyMatch(i -> i.getProductId().equals(3L)));
            assertTrue(cart.getCartItems().stream().anyMatch(i -> i.getProductId().equals(4L)));
            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartRepository).save(cart);
            verify(cartMapper).toCartResponseDto(cart);
        }

        @Test
        void shouldThrowException_when_repositoryFails() {
            cart.setCartItems(new HashSet<>());
            doReturn(cart).when(cartProvider).findCartByAuthUser(userId);
            doThrow(new RuntimeException("DB error")).when(cartRepository).save(any());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> cartService.addItems(new AddCartRequestDto(addCartItemRequestDtos), userId));

            assertEquals("DB error", ex.getMessage());
            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartRepository).save(cart);
            verifyNoInteractions(cartMapper);
        }

    }

    @Nested
    class DeleteItemFromCartTest {

        @Test
        void shouldThrowConstraintViolationException_when_cartItemsIsNull() {
            RemoveItemsRequestDto request = new RemoveItemsRequestDto(null);

            Set<ConstraintViolation<RemoveItemsRequestDto>> violations = validator.validate(request);

            assertEquals(2, violations.size());
            Set<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            assertTrue(messages.contains(CartExceptionMP.REMOVING_LIST_IS_EMPTY));
            assertTrue(messages.contains(CartExceptionMP.REMOVING_LIST_IS_NULL));
            verifyNoInteractions(cartProvider, cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldThrowConstraintViolationException_when_cartItemsIsEmpty() {
            RemoveItemsRequestDto request = new RemoveItemsRequestDto(Collections.emptySet());

            Set<ConstraintViolation<RemoveItemsRequestDto>> violations = validator.validate(request);

            assertEquals(1, violations.size());
            Set<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            assertTrue(messages.contains(CartExceptionMP.REMOVING_LIST_IS_EMPTY));
            assertFalse(messages.contains(CartExceptionMP.REMOVING_LIST_IS_NULL));
            verifyNoInteractions(cartProvider, cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldThrowUserCartNotFoundException_when_cartNotFound() {
            doThrow(new UserCartNotFoundException(
                    String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId)))
                    .when(cartProvider).findCartByAuthUser(anyLong());

            UserCartNotFoundException exception = assertThrows(UserCartNotFoundException.class, () ->
                    cartService.removeItems(removeItemsRequestDto, userId));

            assertEquals(String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId), exception.getMessage());
            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verifyNoInteractions(cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldThrowException_when_repositoryFails() {
            doReturn(cart).when(cartProvider).findCartByAuthUser(userId);
            doThrow(new RuntimeException("DB error")).when(cartRepository).save(any(Cart.class));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> cartService.removeItems(new RemoveItemsRequestDto(Set.of(1L, 2L)), userId));

            assertEquals("DB error", exception.getMessage());
            verify(cartProvider, times(1)).findCartByAuthUser(userId);
            verify(cartRepository, times(1)).save(any(Cart.class));
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verifyNoMoreInteractions(cartProvider, cartRepository);
            verifyNoInteractions(cartMapper);
        }

        @Test
        void shouldDeleteItemsFromCart() {
            cart.setCartItems(cartItems);
            doReturn(cart).when(cartProvider).findCartByAuthUser(userId);
            doReturn(cart).when(cartRepository).save(any(Cart.class));
            doReturn(new CartResponseDto(new HashSet<>() {{
                add(new CartItemResponseDto(3L, "name3", 1,
                        BigDecimal.valueOf(30), BigDecimal.valueOf(30), "imag3"));
                add(new CartItemResponseDto(4L, "name4", 1,
                        BigDecimal.valueOf(10), BigDecimal.valueOf(10), "imag4"));
            }}, BigDecimal.valueOf(40))).when(cartMapper).toCartResponseDto(any());

            CartResponseDto response = cartService.removeItems(removeItemsRequestDto, userId);

            assertNotNull(response);
            assertEquals(2, response.cartItems().size());
            assertEquals(BigDecimal.valueOf(40), response.totalPrice());
            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartRepository).save(cart);
            verify(cartMapper).toCartResponseDto(cart);
            verifyNoMoreInteractions(cartProvider, cartRepository, cartMapper);
            verifyNoInteractions(priceUpdatableService);
        }
    }

    @Nested
    class ClearCartTest {

        @Test
        void shouldThrowUserCartNotFoundException_when_cartNotFound() {
            doThrow(new UserCartNotFoundException(String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId)))
                    .when(cartProvider).findCartByAuthUser(anyLong());

            UserCartNotFoundException exception = assertThrows(UserCartNotFoundException.class, () -> cartService.clear(userId));

            assertEquals(String.format(CartExceptionMP.USER_CART_WAS_NOT_FOUND, userId), exception.getMessage());
            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartProvider).findCartByAuthUser(longArgumentCaptor.capture());
            assertEquals(userId, longArgumentCaptor.getValue());
            verifyNoInteractions(cartRepository, cartMapper, priceUpdatableService);
        }

        @Test
        void shouldClearCart() {
            cart.setCartItems(cartItems);
            doReturn(cart).when(cartProvider).findCartByAuthUser(userId);

            cartService.clear(userId);

            verify(cartProvider).findCartByAuthUser(userId);
            verify(cartRepository, times(1)).save(any(Cart.class));
            verifyNoMoreInteractions(cartProvider, cartRepository);
        }
    }
}