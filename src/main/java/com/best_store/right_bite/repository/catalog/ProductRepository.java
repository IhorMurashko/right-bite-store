package com.best_store.right_bite.repository.catalog;

import com.best_store.right_bite.constant.bmi.BMICategory;
import com.best_store.right_bite.dto.catalog.product.ProductPriceDto;
import com.best_store.right_bite.model.catalog.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProductPriceDto> getProductPriceByProductIds(@Param("productIds") Set<Long> productIds);

    /**
     * Repository for managing {@link Product} entities.
     * Provides specific database access methods for {@link Product}, including custom queries.
     *
     * @author Ihor Murashko
     */
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c.indexBody= :bmiCategory")
    List<Product> getProductByBMICategory(@Param("bmiCategory") BMICategory bmiCategory);

    List<Product> findAllByIdIn(Collection<Long> ids);
}
