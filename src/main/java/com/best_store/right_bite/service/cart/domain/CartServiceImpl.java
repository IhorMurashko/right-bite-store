package com.best_store.right_bite.service.cart.domain;

import com.best_store.right_bite.constant.jpa.JpaConstraints;
import com.best_store.right_bite.dto.cart.request.addToCart.AddCartItemRequestDto;
import com.best_store.right_bite.dto.cart.request.addToCart.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.removeFromCart.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.db.InternalDataBaseConnectionException;
import com.best_store.right_bite.mapper.cart.CartMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.service.cart.helper.CartProvider;
import com.best_store.right_bite.service.cart.price.PriceUpdatableService;
import com.best_store.right_bite.utils.priceCalculator.CartCalculateUtil;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final PriceUpdatableService priceUpdatableService;
    private final CartProvider cartProvider;

    @Cacheable(value = "cart", key = "#userId")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    @Override
    public CartResponseDto getUserCart(@NonNull Long userId) {
        Cart cart = cartProvider.findCartByAuthUser(userId);
        int attempts = 0;
        while (true) {
            try {
                int modifications = priceUpdatableService.refreshCartPrices(userId);
                log.debug("modifications counter is: {}", modifications);
                if (modifications > 0) {
                    CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
                    log.debug("User cart was modified: {} items updated", modifications);
                    log.info("user's cart prices were modified");
                    cart = cartRepository.save(cart);
                }
                return cartMapper.toCartResponseDto(cart);
            } catch (OptimisticLockException ex) {
                attempts++;
                log.warn("OptimisticLockException was caught, retrying");
                if (attempts >= JpaConstraints.MAX_RETRY_ATTEMPTS) {
                    log.error("OptimisticLockException was caught, retry attempts exceeded");
                    throw new InternalDataBaseConnectionException(String.format(
                            ExceptionMessageProvider.OPTIMISTIC_LOCKING_EXCEPTION, userId
                    ));
                }
            }
        }


    }

    @CachePut(value = "cart", key = "#userId")
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public CartResponseDto addItems(@NonNull @Valid AddCartRequestDto addCartItems, @NonNull Long userId) {
        log.debug("Request contains size {}", addCartItems.cartItems().size());
        Cart cart = cartProvider.findCartByAuthUser(userId);
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
        cartRepository.save(cart);
        return cartMapper.toCartResponseDto(cart);
    }

    @CacheEvict(value = "cart", key = "#userId")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public CartResponseDto removeItems(@NonNull @Valid RemoveItemsRequestDto removeItems, @NonNull Long userId) {
        Cart cart = cartProvider.findCartByAuthUser(userId);
        if (removeItems.idsToRemove().isEmpty()) {
            log.warn("No items to remove for user {}", cart.getUser().getId());
        } else {
            cart.removeItemsByProductIds(ci -> removeItems.idsToRemove().contains(ci.getProductId()));
            BigDecimal totalPrice = CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
            cart.setTotalPrice(totalPrice);
            log.debug("Total price after removing items is: {} for user: {}", totalPrice, cart.getUser().getId());
            log.info("Removed {} items from cart for user {}", removeItems.idsToRemove().size(), cart.getUser().getId());
            cartRepository.save(cart);
        }
        return cartMapper.toCartResponseDto(cart);
    }

    @CacheEvict(value = "cart", key = "#userId")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void clear(@NonNull Long userId) {
        Cart cart = cartProvider.findCartByAuthUser(userId);
        cart.clear();
        log.debug("Cleared cart for user {}", cart.getUser().getId());
        BigDecimal totalPrice = CartCalculateUtil.calculateTotalPriceOfCart(cart, 2, RoundingMode.HALF_UP);
        cart.setTotalPrice(totalPrice);
        log.info("Cart for user {} has been cleared", cart.getUser().getId());
        cartRepository.save(cart);
    }
}
