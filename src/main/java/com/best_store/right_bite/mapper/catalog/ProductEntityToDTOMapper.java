package com.best_store.right_bite.mapper.catalog;


import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.model.catalog.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductEntityToDTOMapper implements BaseMapper<Product, ProductDTO> {
    private final BrandEntityToDTOMapper brandEntityToDTOMapper;
    private final ProductSalesEntityToDTOMapper productSalesEntityToDTOMapper;
    private final ImageEntityToDTOMapper imageEntityToDTOMapper;
    private final CategoryEntityToDTOMapper categoryEntityToDTOMapper;
    private final ProducerEntityToDTOMapper producerEntityToDTOMapper;

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
