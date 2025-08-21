package com.best_store.right_bite.mapper.catalog;

import com.best_store.right_bite.dto.catalog.ProducerDTO;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.catalog.Producer;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserFieldAdapter.class)
public interface ProducerMapper extends BaseMapper<Producer, ProducerDTO> {

    @Override
    ProducerDTO toDTO(Producer producer);
}
