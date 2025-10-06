package com.best_store.right_bite.mapper.order.orderDeliveryDetails;


import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.order.OrderDeliveryDetails;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderDeliveryDetailsMapper extends BaseMapper<OrderDeliveryDetails, OrderDeliveryDetailsDto> {
}
