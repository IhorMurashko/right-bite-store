package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryDTO> {

    @Override
    CategoryDTO toDTO(Category category);
}
