package com.best_store.right_bite.service.catalog.brandDomain;

import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.mapper.catalog.BrandMapper;
import com.best_store.right_bite.repository.catalog.BrandRepository;
import lombok.RequiredArgsConstructor;
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
