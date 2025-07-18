package com.best_store.right_bite.mapper.catalog;


import com.best_store.right_bite.dto.catalog.*;
import com.best_store.right_bite.model.catalog.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component("ProductEntityToDTO")
@RequiredArgsConstructor
public class ProductEntityToDTOMapper implements BaseMapper<Product, ProductDTO> {

    @Qualifier("BrandEntityToDTO")
    private final BaseMapper<Brand, BrandDTO> brandEntityToDTOMapper;

    @Qualifier("ProductSalesEntityToDTO")
    private final BaseMapper<ProductSales, ProductSalesDTO> productSalesEntityToDTOMapper;

    @Qualifier("ImageEntityToDTO")
    private final BaseMapper<Image, ImageDTO> imageEntityToDTOMapper;

    @Qualifier("CategoryEntityToDTO")
    private final BaseMapper<Category, CategoryDTO> categoryEntityToDTOMapper;

    @Qualifier("ProducerEntityToDTO")
    private final BaseMapper<Producer, ProducerDTO> producerEntityToDTOMapper;


    @Override
    public ProductDTO map(Product obj) {
        return ProductDTO.builder()
                .id(obj.getId())
                .productName(obj.getProductName())
                .description(obj.getDescription())
                .price(obj.getPrice())
                .kcal(obj.getKcal())
                .brand(
                        brandEntityToDTOMapper.map(obj.getBrand())
                )
                .quantityInStock(obj.getQuantityInStock())
                .ratingCount(obj.getRatingCount())
                .rating(obj.getRating())
                .productSales(
                        obj.getProductSales().stream().map(productSalesEntityToDTOMapper::map).collect(Collectors.toList())
                )
                .images(
                      obj.getImages().stream().map(imageEntityToDTOMapper::map).collect(Collectors.toList())
                )
                .categories(
                    obj.getCategories().stream().map(categoryEntityToDTOMapper::map).collect(Collectors.toSet())
                )
                .producer(
                        producerEntityToDTOMapper.map(obj.getProducer())
                )
                .build();

    }
}
