package com.best_store.right_bite.service.cart.application;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.mapper.cart.CartMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.security.exception.InvalidTokenSubjectException;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import com.best_store.right_bite.service.cart.domain.CartService;
import com.best_store.right_bite.service.cart.price.PriceUpdatableService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.security.AuthenticationParserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartFacadeImplTest {

    @Mock
    private CartService cartService;
    @Mock
    private UserCrudService userCrudService;
    @Mock
    private CartMapper cartMapper;
    @Mock
    private PriceUpdatableService priceUpdatableService;
    @Mock
    private AuthenticationParserUtil authenticationParserUtil;
    @InjectMocks
    private CartFacadeImpl cartFacade;

    private Authentication authentication;
    private User user;
    private Long userId;
    private String email;
    private Set<Role> roles;
    private Optional<Cart> existingUserCart;
    private Cart newUserCart;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @BeforeEach
    void setUp() {
        this.authentication = new UsernamePasswordAuthenticationToken(
                new JwtPrincipal(String.valueOf(userId), email), null,
                Collections.singleton(new SimpleGrantedAuthority(RoleName.ROLE_USER.name()))
        );
        this.user = new User(email, "pass45",
                true, true, true, true,
                roles, null, null);
        this.user.setId(userId);
        this.userId = 1L;
        this.email = "email@email.com";
        this.roles = Collections.singleton(new Role(1L, RoleName.ROLE_USER, null));
        this.existingUserCart = Optional.of(new Cart(user));
        this.newUserCart = new Cart(user);
    }


    @Nested
    @DisplayName("find cart by user")
    class FindCartByAuthUser {

        @Test
        @DisplayName("get existing user cart")
        void shouldReturnExistingUserCart_when_userWasFound() {
            doReturn(userId).when(authenticationParserUtil).getUserLongIdFromAuthentication(authentication);
            doReturn(user).when(userCrudService).findById(userId);
            doReturn(existingUserCart).when(cartService).getCartByUserId(userId);

            Cart cart = cartFacade.findCartByAuthUser(authentication);
            verify(cartService).getCartByUserId(longArgumentCaptor.capture());

            assertNotNull(cart);
            assertTrue(existingUserCart.isPresent());
            assertSame(existingUserCart.get(), cart);
            assertEquals(user, cart.getUser());
            assertEquals(userId, longArgumentCaptor.getValue());

            verify(authenticationParserUtil, times(1)).getUserLongIdFromAuthentication(authentication);
            verify(userCrudService, times(1)).findById(userId);
            verify(cartService, times(1)).getCartByUserId(userId);
            verifyNoMoreInteractions(authenticationParserUtil, userCrudService, cartService);

        }

        @Test
        @DisplayName("create new user cart")
        void shouldReturnNewUserCart_when_userWasNotFound() {
            doReturn(userId).when(authenticationParserUtil).getUserLongIdFromAuthentication(authentication);
            doReturn(user).when(userCrudService).findById(userId);
            doReturn(Optional.empty()).when(cartService).getCartByUserId(userId);
            doReturn(newUserCart).when(cartService).save(any(Cart.class));

            Cart cart = cartFacade.findCartByAuthUser(authentication);
            verify(cartService).getCartByUserId(longArgumentCaptor.capture());

            assertNotNull(cart);
            assertSame(newUserCart, cart);
            assertEquals(user, cart.getUser());
            assertEquals(newUserCart.getId(), cart.getId());
            assertEquals(userId, longArgumentCaptor.getValue());

            verify(authenticationParserUtil, times(1)).getUserLongIdFromAuthentication(authentication);
            verify(userCrudService, times(1)).findById(userId);
            verify(cartService, times(1)).getCartByUserId(userId);
            verify(cartService, times(1)).save(any(Cart.class));
            verifyNoMoreInteractions(authenticationParserUtil, userCrudService, cartService);
        }


        @Test
        @DisplayName("throw exception when token subject has wrong type")
        void shouldThrowInvalidTokenSubjectException_when_tokenSubjectHasWrongType() {
            doThrow(new InvalidTokenSubjectException(
                    ExceptionMessageProvider.INVALID_TOKEN_SUBJECT
            )).when(authenticationParserUtil)
                    .getUserLongIdFromAuthentication(authentication);

            InvalidTokenSubjectException exception = assertThrows(InvalidTokenSubjectException.class,
                    () -> cartFacade.findCartByAuthUser(authentication));

            assertNotNull(exception);
            assertEquals(ExceptionMessageProvider.INVALID_TOKEN_SUBJECT, exception.getMessage());

            verify(authenticationParserUtil, times(1)).getUserLongIdFromAuthentication(authentication);
            verifyNoMoreInteractions(authenticationParserUtil);
            verifyNoInteractions(userCrudService, cartService);
        }

        @Test
        @DisplayName("throw exception when user was not found")
        void shouldThrowUserNotFoundException_when_userWasNotFound_and_userIsNull() {
            doReturn(userId).when(authenticationParserUtil).getUserLongIdFromAuthentication(authentication);
            doThrow(new UserNotFoundException(
                    ExceptionMessageProvider.USER_ID_NOT_FOUND)).when(userCrudService).findById(userId);

            UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                    () -> cartFacade.findCartByAuthUser(authentication));

            assertNotNull(exception);
            assertEquals(ExceptionMessageProvider.USER_ID_NOT_FOUND, exception.getMessage());
            verify(userCrudService).findById(longArgumentCaptor.capture());
            verify(authenticationParserUtil, times(1)).getUserLongIdFromAuthentication(authentication);
            verify(userCrudService, times(1)).findById(userId);
            verifyNoMoreInteractions(userCrudService, authenticationParserUtil);
            verifyNoInteractions(cartService);
        }
    }


}