package com.best_store.right_bite.dto.catalog;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductFilterRequest {

    private String categoryName;

    private String brand;

    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    private Character aZ;

    private String sortBy; // "priceAsc", "priceDesc", "rating", "popular"


    private int page = 0;
    private int size = 24;

}
