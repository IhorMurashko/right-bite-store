package com.best_store.right_bite.service.catalog;


import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.mapper.catalog.BaseMapper;
import com.best_store.right_bite.model.catalog.Category;
import com.best_store.right_bite.repository.catalog.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Qualifier("CategoryEntityToDTO")
    private final BaseMapper<Category, CategoryDTO> categoryEntityToDTOMapper;

    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(categoryEntityToDTOMapper::map).collect(Collectors.toList());
    }

}
