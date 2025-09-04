package com.best_store.right_bite.mapper.order.response;

import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.mapper.order.orderDeliveryDetails.OrderDeliveryDetailsMapper;
import com.best_store.right_bite.mapper.order.orderItem.OrderItemDtoMapper;
import com.best_store.right_bite.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {OrderDeliveryDetailsMapper.class, OrderItemDtoMapper.class}
)
public interface OrderResponseMapper extends BaseMapper<Order, OrderResponseDto> {
}
