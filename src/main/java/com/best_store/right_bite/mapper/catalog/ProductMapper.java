package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProductDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Category;
import com.best_store.right_bite.model.catalog.Product;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserFieldAdapter.class)
public interface ProductMapper extends BaseMapper<Product, ProductDTO> {

    @Override
    @Mapping(target = "indexBody", source = "categories")
    ProductDTO toDTO(Product product);

    default String mapIndexBody(Set<Category> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return categories.iterator().next().getIndexBody().getCategory(); // или другой выбор
    }
}
