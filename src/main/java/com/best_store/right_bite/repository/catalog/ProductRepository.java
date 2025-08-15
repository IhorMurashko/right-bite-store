package com.best_store.right_bite.repository.catalog;

import com.best_store.right_bite.dto.catalog.product.ProductPriceDto;
import com.best_store.right_bite.model.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Retrieves a list of {@link ProductPriceDto} for the given set of product IDs.
     *
     * @param productIds set of product IDs to fetch prices for
     * @return list of product price DTOs
     */
    @Query("SELECT new com.best_store.right_bite.dto.catalog.product.ProductPriceDto(" +
            "p.id, p.price) FROM Product p WHERE p.id IN :productIds")
    List<ProductPriceDto> getProductPriceByProductIds(@Param("productIds") Set<Long> productIds);
}
