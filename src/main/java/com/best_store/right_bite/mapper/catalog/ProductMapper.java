package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductDTO> {

    @Override
    ProductDTO toDTO(Product product);
}
