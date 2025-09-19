package com.best_store.right_bite.service.cart.price;

import com.best_store.right_bite.constant.constraint.products.ProductConstraints;
import com.best_store.right_bite.constant.jpa.JpaConstraints;
import com.best_store.right_bite.dto.catalog.product.ProductPriceDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.cart.UserCartNotFoundException;
import com.best_store.right_bite.exception.db.InternalDataBaseConnectionException;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.repository.catalog.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of {@link PriceUpdatableService} that updates item prices
 * in a cart using the latest data from the product repository.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriceUpdatableServiceImpl implements PriceUpdatableService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    @Override
    public int refreshCartPrices(@NonNull Long userId) {
        int attempts = 0;
        int modifyingCounter = 0;
        while (true) {
            try {
                Cart cart = cartRepository.findCartByUserId(userId)
                        .orElseThrow(() -> new UserCartNotFoundException(String.format(
                                ExceptionMessageProvider.USER_CART_WAS_NOT_FOUND, userId
                        )));
                if (cart.getCartItems().isEmpty()) {
                    return modifyingCounter;
                }
                LocalDateTime now = LocalDateTime.now();
                Set<Long> ids = cart.getCartItems().stream().map(CartItem::getProductId).collect(Collectors.toSet());
                log.debug("refresh product ids size is: {}", ids.size());
                Map<Long, BigDecimal> actualPrices = productRepository.getProductPriceByProductIds(ids).stream()
                        .collect(Collectors.toMap(ProductPriceDto::id, ProductPriceDto::price));
                for (CartItem cartItem : cart.getCartItems()) {
                    Duration duration = Duration.between(cartItem.getPriceSnapshotTime(), now);
                    long durationMinutes = duration.toMinutes();
                    log.debug("item snapshot duration in minutes is {}", durationMinutes);
                    if (durationMinutes > ProductConstraints.PRICE_EXPIRATION_MINUTES) {
                        BigDecimal currentPrice = actualPrices.get(cartItem.getProductId());
                        log.debug("current price is {} for product {}", currentPrice, cartItem.getProductName());
                        if (!Objects.equals(currentPrice, cartItem.getUnitPriceSnapshot())) {
                            log.debug("actualPrices are different for the product {}, snapshot price is: {}, new price is {}",
                                    cartItem.getProductId(), cartItem.getUnitPriceSnapshot(), currentPrice);
                            cartItem.setUnitPriceSnapshot(currentPrice.setScale(2, RoundingMode.HALF_UP));
                            cartItem.setTotalPrice(currentPrice
                                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                                    .setScale(2, RoundingMode.HALF_UP));
                            cartItem.setPriceSnapshotTime(LocalDateTime.now());
                            log.debug("current item with product id: {} was updated", cartItem.getProductId());
                            modifyingCounter++;
                            log.debug("modifying counter was updated");
                        }
                    }
                }
                if (modifyingCounter > 0) {
                    log.info("Prices were updated for {} items in cart for user {}", modifyingCounter, userId);
                    cartRepository.save(cart);
                }
                return modifyingCounter;
            } catch (OptimisticLockException ex) {
                attempts++;
                log.warn("OptimisticLockException was caught, retrying");
                if (attempts >= JpaConstraints.MAX_RETRY_ATTEMPTS) {
                    log.error("OptimisticLockException was caught, retry attempts exceeded");
                    throw new InternalDataBaseConnectionException(ex.getMessage());
                }
            }
        }
    }
}