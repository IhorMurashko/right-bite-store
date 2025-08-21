package com.best_store.right_bite.model.catalog;

import com.best_store.right_bite.constant.bmi.BMICategory;
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


    private String productName;
    private BigDecimal price;
    private String description;
    private Double kcal;
    private Integer quantityInStock;

    private Double rating;
    private Integer ratingCount;

    private Double calories;
    private Double carbs;
    private Double protein;
    private Double fat;
    private Double fiber;
    private String vitamins;


    @OneToMany(mappedBy = "product")
    private List<ProductSales> productSales;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<Image> images;

    @ManyToMany()
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;


}
