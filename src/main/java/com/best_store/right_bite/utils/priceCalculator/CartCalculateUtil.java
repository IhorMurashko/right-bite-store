package com.best_store.right_bite.utils.priceCalculator;

import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.cart.CartItem;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility class providing methods for calculating cart and cart item totals.
 * All methods are static and designed to simplify price calculations.
 */
@UtilityClass
public class CartCalculateUtil {
    /**
     * Calculates the total price of a given cart by summing up total prices of its items.
     *
     * @param cart         the cart entity whose total price is calculated
     * @param newScale     the scale (number of decimal places) for rounding; defaults to 2 if invalid
     * @param roundingMode the rounding mode; defaults to {@link RoundingMode#HALF_UP} if null
     * @return the total price of the cart as {@link BigDecimal}, rounded accordingly
     */
    public BigDecimal calculateTotalPriceOfCart(@NonNull Cart cart, int newScale, RoundingMode roundingMode) {
        if (newScale < 0 || newScale > 100) {
            newScale = 2;
        }
        if (roundingMode == null) {
            roundingMode = RoundingMode.HALF_UP;
        }
        return cart.getCartItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(newScale, roundingMode);
    }

    /**
     * Calculates the total price for a single cart item based on its unit price and quantity.
     *
     * @param unitPrice the price per unit of the item
     * @param quantity  the quantity of the item
     * @return the total price for the item as {@link BigDecimal}, rounded to 2 decimal places
     */
    public BigDecimal calculateTotalItemPrice(@NonNull BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
