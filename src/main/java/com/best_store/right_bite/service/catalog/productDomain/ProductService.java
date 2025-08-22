package com.best_store.right_bite.service.catalog.productDomain;

import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductFilterRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface defining operations for managing products.
 * Provides methods to retrieve, filter, and paginate product data.
 *
 */
public interface ProductService {
    /**
     * Interface providing operations for managing and retrieving product information.
     * Includes capabilities for fetching all products, filtering, and advanced queries.
     */
    List<ProductDTO> getAllProduct();

    /**
     * Interface defining operations for managing products.
     * Provides methods to retrieve, filter, and paginate product data.
     */
    Page<ProductDTO> getAllProductPageable(ProductFilterRequest productFilterRequest);

    /**
     * Interface defining operations for managing product data.
     * Provides functionality to retrieve product information by its unique identifier.
     */
    ProductDTO getProductDTOById(Long id);

    /**
     * Interface defining operations for managing products.
     * Provides methods to retrieve, filter, and categorize product data.
     */
    List<ProductDTO> getProductsByBMICategory(@NotNull BMICategory bmiCategory);
}
