package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProducerDTO;
import com.best_store.right_bite.model.catalog.Producer;
import org.springframework.stereotype.Component;

@Component("ProducerEntityToDTO")
public class ProducerEntityToDTOMapper implements BaseMapper<Producer, ProducerDTO> {
    @Override
    public ProducerDTO map(Producer obj) {
        return ProducerDTO.builder()
                .producerName(obj.getProducerName())
                .build();
    }
}
