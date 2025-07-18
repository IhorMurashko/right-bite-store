package com.best_store.right_bite.mapper.catalog;


import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.model.catalog.Brand;
import org.springframework.stereotype.Component;

@Component("BrandEntityToDTO")
public class BrandEntityToDTOMapper implements BaseMapper<Brand, BrandDTO>{
    @Override
    public BrandDTO map(Brand obj) {
        return BrandDTO.builder()
                .brandName(obj.getBrandName())
                .build();
    }
}
