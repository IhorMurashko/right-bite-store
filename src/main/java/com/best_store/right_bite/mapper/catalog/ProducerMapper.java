package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProducerDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Producer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProducerMapper extends BaseMapper<Producer, ProducerDTO> {

    @Override
    ProducerDTO toDTO(Producer producer);
}
