package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ImageDTO;
import com.best_store.right_bite.model.catalog.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageEntityToDTOMapper implements BaseMapper<Image, ImageDTO>{
    @Override
    public ImageDTO map(Image obj) {
        return ImageDTO.builder()
                .url(obj.getUrl())
                .productId(obj.getProduct().getId())
                .build();
    }
}
