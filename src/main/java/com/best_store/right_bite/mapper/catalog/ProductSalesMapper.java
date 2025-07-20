package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProductSalesDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.ProductSales;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductSalesMapper extends BaseMapper<ProductSales, ProductSalesDTO> {

    @Mapping(source = "product.id", target = "productId")
    @Override
    ProductSalesDTO toDTO(ProductSales productSales);
}
