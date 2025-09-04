package com.best_store.right_bite.service.cart.application;

import com.best_store.right_bite.dto.cart.request.addToCart.AddCartItemRequestDto;
import com.best_store.right_bite.dto.cart.request.addToCart.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.removeFromCart.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.mapper.cart.CartMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.service.cart.domain.CartService;
import com.best_store.right_bite.service.cart.price.PriceUpdatableService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.priceCalculator.CartCalculateUtil;
import com.best_store.right_bite.utils.security.AuthenticationParserUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CartFacade} providing business logic for managing user carts.
 * Handles cart retrieval, creation, price updates, and persistence.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CartFacadeImpl implements CartFacade {

    private final CartService cartService;
    private final UserCrudService userCrudService;
    private final CartMapper cartMapper;
    private final PriceUpdatableService priceUpdatableService;
    private final AuthenticationParserUtil authenticationParserUtil;

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public Cart findCartByAuthUser(@NonNull Authentication authentication) {
        Long userId = authenticationParserUtil.extractUserLongIdFromAuthentication(authentication);
        User user = userCrudService.findById(userId);
        log.debug("user with id: {} was found", userId);
        Optional<Cart> optionalCart = cartService.getCartByUserId(userId);
        if (optionalCart.isEmpty()) {
            log.debug("user cart is not present");
            Cart created = new Cart(user);
            Cart persistedCart = cartService.save(created);
            optionalCart = Optional.of(persistedCart);
            log.info("new user cart was created");
        }
        return optionalCart.get();
    }

    /**
     * {@inheritDoc}
     */
    @Cacheable(value = "cart", key = "@authenticationParserUtil.extractUserLongIdFromAuthentication(#authentication)")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public CartResponseDto getUserCart(@NonNull Authentication authentication) {
        Cart cart = findCartByAuthUser(authentication);
        int modifications = priceUpdatableService.refreshCartPrices(cart);
        log.debug("modifications counter is: {}", modifications);
        if (modifications > 0) {
            CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
            cartService.save(cart);
            log.debug("User cart was modified: {} items updated", modifications);
            log.info("user's cart prices were modified");
        }
        log.info("user cart was found successfully for user {}", cart.getUser().getId());
        return cartMapper.toCartResponseDto(cart);
    }

    /**
     * {@inheritDoc}
     */
    @CachePut(value = "cart", key = "@authenticationParserUtil.extractUserLongIdFromAuthentication(#authentication)")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public CartResponseDto addItems(@NonNull @Valid AddCartRequestDto addCartItems, @NonNull Authentication authentication) {
        log.debug("Request contains size {}", addCartItems.cartItems().size());
        Cart cart = findCartByAuthUser(authentication);
        Map<Long, CartItem> existingItemsMap = cart.getCartItems().stream()
                .collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
        log.debug("existingItemsMap size: {}", existingItemsMap.size());
        for (AddCartItemRequestDto dto : addCartItems.cartItems()) {
            CartItem existingItem = existingItemsMap.get(dto.productId());
            if (existingItem != null) {
                log.debug("item with product id: {} is existed", dto.productId());
                existingItem.setQuantity(dto.quantity());
                existingItem.setUnitPriceSnapshot(dto.unitPriceSnapshot());
                existingItem.setTotalPrice(CartCalculateUtil
                        .calculateTotalItemPrice(dto.unitPriceSnapshot(), dto.quantity()));
                existingItem.setThumbnailUrl(dto.thumbnailUrl());
                log.info("item with product id: {} has been updated", dto.productId());
            } else {
                cart.addItem(new CartItem(
                        dto.productId(),
                        dto.productName(),
                        dto.quantity(),
                        dto.unitPriceSnapshot(),
                        CartCalculateUtil.calculateTotalItemPrice(dto.unitPriceSnapshot(), dto.quantity()),
                        dto.thumbnailUrl(),
                        LocalDateTime.now(),
                        cart));
                log.info("item with product id: {} has been added", dto.productId());
            }
        }
        BigDecimal totalPrice = CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
        cart.setTotalPrice(totalPrice);
        log.debug("total price: {}", totalPrice);
        log.info("Added {} items to cart for user {}", cart.getCartItems().size(), cart.getUser().getId());
        cartService.save(cart);
        return cartMapper.toCartResponseDto(cart);
    }

    /**
     * {@inheritDoc}
     */
    @CacheEvict(value = "cart", key = "@authenticationParserUtil.extractUserLongIdFromAuthentication(#authentication)")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public CartResponseDto removeItems(@NonNull @Valid RemoveItemsRequestDto removeItems, @NonNull Authentication authentication) {
        Cart cart = findCartByAuthUser(authentication);
        if (removeItems.idsToRemove().isEmpty()) {
            log.warn("No items to remove for user {}", cart.getUser().getId());
        } else {
            cart.removeItemsByProductIds(ci -> removeItems.idsToRemove().contains(ci.getProductId()));
            BigDecimal totalPrice = CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
            cart.setTotalPrice(totalPrice);
            log.debug("Total price after removing items is: {} for user: {}", totalPrice, cart.getUser().getId());
            log.info("Removed {} items from cart for user {}", removeItems.idsToRemove().size(), cart.getUser().getId());
            cartService.save(cart);
        }
        return cartMapper.toCartResponseDto(cart);
    }

    /**
     * {@inheritDoc}
     */
    @CacheEvict(value = "cart", key = "@authenticationParserUtil.extractUserLongIdFromAuthentication(#authentication)")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void clear(@NonNull Authentication authentication) {
        Cart cart = findCartByAuthUser(authentication);
        cart.clear();
        log.debug("Cleared cart for user {}", cart.getUser().getId());
        BigDecimal totalPrice = CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
        cart.setTotalPrice(totalPrice);
        log.info("Cart for user {} has been cleared", cart.getUser().getId());
        cartService.save(cart);
    }
}
