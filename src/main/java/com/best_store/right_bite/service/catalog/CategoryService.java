package com.best_store.right_bite.service.catalog;


import com.best_store.right_bite.dto.catalog.CategoryDTO;
import com.best_store.right_bite.mapper.catalog.CategoryEntityToDTOMapper;
import com.best_store.right_bite.repository.catalog.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryEntityToDTOMapper categoryEntityToDTOMapper;

    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream().map(categoryEntityToDTOMapper::map).collect(Collectors.toList());
    }

}
