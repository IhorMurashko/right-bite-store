package com.best_store.right_bite.repository.catalog;

import com.best_store.right_bite.model.catalog.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
}
