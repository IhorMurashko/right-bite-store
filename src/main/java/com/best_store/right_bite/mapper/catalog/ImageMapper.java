package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ImageDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper extends BaseMapper<Image, ImageDTO> {

    @Mapping(source = "product.id", target = "productId")
    @Override
    ImageDTO toDTO(Image image);
}
