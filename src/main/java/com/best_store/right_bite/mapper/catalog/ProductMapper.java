package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Category;
import com.best_store.right_bite.model.catalog.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper<Product, ProductDTO> {

    @Override
    @Mapping(target = "indexBody", source = "categories")
    ProductDTO toDTO(Product product);

    default String mapIndexBody(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return categories.iterator().next().getIndexBody().getCategory(); // или другой выбор
    }
}
