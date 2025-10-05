package com.best_store.right_bite.mapper.adminPanel;


import com.best_store.right_bite.dto.adminPanel.order.OrderDTO;
import com.best_store.right_bite.mapper.order.orderItem.OrderItemDtoMapper;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.model.order.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DefaultUserInfoDtoMapper.class, OrderItemDtoMapper.class},
unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderDTO toDTO(Order order);

    Order toEntity(OrderDTO orderDTO);

}
