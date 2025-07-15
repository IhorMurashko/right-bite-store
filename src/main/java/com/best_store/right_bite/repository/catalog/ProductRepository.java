package com.best_store.right_bite.repository.catalog;

import com.best_store.right_bite.model.catalog.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
