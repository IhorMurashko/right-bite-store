package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.model.catalog.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityToDTOMapper implements BaseMapper<Category, CategoryDTO>{
    @Override
    public CategoryDTO map(Category obj) {
        return CategoryDTO.builder()
                .category_name(obj.getCategoryName())
                .image(obj.getImage())
                .build();
    }
}
