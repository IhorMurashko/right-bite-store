package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProductSalesDTO;
import com.best_store.right_bite.model.catalog.ProductSales;
import org.springframework.stereotype.Component;

@Component("ProductSalesEntityToDTO")
public class ProductSalesEntityToDTOMapper implements BaseMapper<ProductSales, ProductSalesDTO>{
    @Override
    public ProductSalesDTO map(ProductSales obj) {
        return ProductSalesDTO.builder()
                .productId(obj.getId())
                .quantity(obj.getQuantity())
                .saleDate(obj.getSaleDate())
                .build();
    }
}
