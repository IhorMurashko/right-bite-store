package com.best_store.right_bite.model.catalog;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends Base {


    String productName;
    BigDecimal price;
    String description;
    Double kcal;
    Integer quantityInStock;

    Double rating;
    Integer ratingCount;

    Double calories;
    Double carbs;
    Double protein;
    Double fat;
    Double fiber;
    String vitamins;


    @OneToMany(mappedBy = "product")
    List<ProductSales> productSales;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    Brand brand;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    List<Image> images;

    @ManyToMany()
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    Producer producer;


}
