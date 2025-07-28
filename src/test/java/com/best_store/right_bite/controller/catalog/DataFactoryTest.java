package com.best_store.right_bite.controller.catalog;

import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.dto.catalog.ProductSalesDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataFactoryTest {
/*
*  /*
    *  Long id;
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
    ProducerDTO producer;*/

    public static List<ProductSalesDTO> productSalesDTO() {
        List<ProductSalesDTO> productSalesDTOList = new ArrayList<>();
        productSalesDTOList.add(new ProductSalesDTO(1L, 1, LocalDateTime.now()));
        return productSalesDTOList;
    }
    public static BrandDTO brandDTO() {
        return BrandDTO.builder()
                .brandName("brand")
                .build();
    }


    public static ProductDTO createSampleProduct() {
        List<ProductSalesDTO> productSalesDTOS = productSalesDTO();


        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setProductName("Test Product");
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setDescription("Test Description");
        product.setKcal(1);
        product.setQuantityInStock(3);
        product.setRating(4.2);
        product.setRatingCount(2);
        product.setProductSales(productSalesDTOS);
        product.setBrand(brandDTO());
        product.setCategories(null);
        product.setProducer(null);
        return product;
    }

    public static ProductDTO createProductWithCustomName(String name) {
        List<ProductSalesDTO> productSalesDTOS = productSalesDTO();


        ProductDTO product = new ProductDTO();
        product.setId(1L);
        product.setProductName(name);
        product.setPrice(BigDecimal.valueOf(99.99));
        product.setDescription("Test Description");
        product.setKcal(1);
        product.setQuantityInStock(3);
        product.setRating(4.2);
        product.setRatingCount(2);
        product.setProductSales(productSalesDTOS);
        product.setBrand(brandDTO());
        product.setCategories(null);
        product.setProducer(null);
        return product;
    }

}
