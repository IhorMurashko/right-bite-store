package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * Provides predefined error messages related to product operations.
 *
 * <p>This utility class centralizes common product-related error messages,
 * ensuring consistency and maintainability across the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new ProductNotFoundException(
 *     String.format(ProductExceptionMP.ID_PRODUCT_NOT_FOUND, productId)
 * );
 * }</pre>
 *
 * <p>Designed as a final utility class to avoid instantiation and inheritance.</p>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class ProductExceptionMP {
    public final String ID_PRODUCT_NOT_FOUND = "Product with id %s not found";
}