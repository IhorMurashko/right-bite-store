package com.best_store.right_bite.service.catalog;

import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.mapper.catalog.BrandEntityToDTOMapper;
import com.best_store.right_bite.repository.catalog.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final BrandEntityToDTOMapper brandEntityToDTOMapper;

    public List<BrandDTO> getAllBrands() {
        return  brandRepository.findAll().stream().map(brandEntityToDTOMapper::map).collect(Collectors.toList());
    }



}
