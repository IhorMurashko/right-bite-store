package com.best_store.right_bite.service.catalog;

import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.mapper.catalog.BrandMapper;
import com.best_store.right_bite.model.catalog.Brand;
import com.best_store.right_bite.repository.catalog.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final BrandMapper brandMapper;

    public List<BrandDTO> getAllBrands() {
        return  brandRepository.findAll().stream().map(brandMapper::toDTO).collect(Collectors.toList());
    }
}
