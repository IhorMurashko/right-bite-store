package com.best_store.right_bite.dto.catalog.product;

import java.math.BigDecimal;

/**
 * DTO representing the price information of a product.
 * Contains the product ID and its current price.
 */
public record ProductPriceDto(
        Long id,
        BigDecimal price
) {
}
