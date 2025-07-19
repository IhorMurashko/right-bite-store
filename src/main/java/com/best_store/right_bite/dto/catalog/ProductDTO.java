package com.best_store.right_bite.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    Long id;
    String productName;
    BigDecimal price;
    String description;
    Integer kcal;
    Integer quantityInStock;
    Double rating;
    Integer ratingCount;


    List<ProductSalesDTO> productSales;
    BrandDTO brand;
    List<ImageDTO> images;
    Set<CategoryDTO> categories;
    ProducerDTO producer;
}
