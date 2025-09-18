package com.best_store.right_bite.service.cart.helper;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartProvider {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public Cart findCartByAuthUser(@NonNull Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(
                        ExceptionMessageProvider.USER_ID_NOT_FOUND, userId)));
        log.debug("user with id: {} was found", userId);
        Optional<Cart> optionalCart = cartRepository.findCartByUserId(userId);
        if (optionalCart.isEmpty()) {
            log.debug("user cart is not present");
            Cart created = new Cart(user);
            Cart savedCart = cartRepository.save(created);
            optionalCart = Optional.of(savedCart);
            log.info("new user cart was created");
        }
        return optionalCart.get();
    }
}
