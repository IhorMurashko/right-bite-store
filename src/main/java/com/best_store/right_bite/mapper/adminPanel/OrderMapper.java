package com.best_store.right_bite.mapper.adminPanel;


import com.best_store.right_bite.dto.adminPanel.order.OrderDTO;
import com.best_store.right_bite.mapper.order.orderDeliveryDetails.OrderDeliveryDetailsMapper;
import com.best_store.right_bite.mapper.order.orderItem.OrderItemDtoMapper;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DefaultUserInfoDtoMapper.class,
        OrderItemDtoMapper.class, OrderDeliveryDetailsMapper.class},
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(source = "orderDeliveryDetails", target = "orderDeliveryDetails")
    OrderDTO toDTO(Order order);

    @Mapping(source = "orderDeliveryDetails", target = "orderDeliveryDetails")
    Order toEntity(OrderDTO orderDTO);

}
