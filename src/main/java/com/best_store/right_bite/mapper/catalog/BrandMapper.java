package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.BrandDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper extends BaseMapper<Brand, BrandDTO> {

    @Override
    BrandDTO toDTO(Brand brand);

}
