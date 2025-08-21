package com.best_store.right_bite.service.catalog.categoryDomain;


import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.mapper.catalog.CategoryMapper;
import com.best_store.right_bite.repository.catalog.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryEntityToDTOMapper;

    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(categoryEntityToDTOMapper::toDTO).collect(Collectors.toList());
    }

}
