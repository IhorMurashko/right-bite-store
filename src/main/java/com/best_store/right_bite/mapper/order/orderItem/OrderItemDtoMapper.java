package com.best_store.right_bite.mapper.order.orderItem;

import com.best_store.right_bite.dto.order.request.OrderItemDto;
import com.best_store.right_bite.mapper.BaseMapper;
import com.best_store.right_bite.model.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemDtoMapper extends BaseMapper<OrderItem, OrderItemDto> {
}
