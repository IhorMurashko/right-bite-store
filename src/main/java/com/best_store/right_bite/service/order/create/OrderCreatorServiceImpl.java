package com.best_store.right_bite.service.order.create;

import com.best_store.right_bite.constant.order.OrderStatus;
import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderDto;
import com.best_store.right_bite.dto.order.request.OrderItemDto;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.exception.exceptions.cart.UserCartNotFoundException;
import com.best_store.right_bite.exception.messageProvider.CartExceptionMP;
import com.best_store.right_bite.exception.exceptions.order.order.EmptyCartException;
import com.best_store.right_bite.mapper.order.orderDeliveryDetails.OrderDeliveryDetailsMapper;
import com.best_store.right_bite.mapper.order.orderItem.OrderItemDtoMapper;
import com.best_store.right_bite.mapper.order.response.OrderResponseMapper;
import com.best_store.right_bite.model.cart.Cart;
import com.best_store.right_bite.model.order.Order;
import com.best_store.right_bite.model.order.OrderDeliveryDetails;
import com.best_store.right_bite.model.order.OrderItem;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.repository.order.OrderRepository;
import com.best_store.right_bite.service.inventory.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class OrderCreatorServiceImpl implements OrderCreatorService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemDtoMapper orderItemDtoMapper;
    private final OrderDeliveryDetailsMapper orderDeliveryDetailsMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final InventoryService inventoryService;

    @Override
    public OrderResponseDto createGuestOrder(@NotNull @Valid OrderDto orderDto, @Nullable User user) {
        log.debug("Creating guest order");
        Map<Long, Integer> productIdsWithQuantity = orderDto.items().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(OrderItemDto::productId, OrderItemDto::quantity));
        try {
            inventoryService.deductInventory(productIdsWithQuantity);
            log.debug("Inventory deducted successfully for guest order");
            Set<OrderItem> itemsSet = orderItemDtoMapper.toEntitySet(orderDto.items());
            OrderDeliveryDetails deliveryDetails = orderDeliveryDetailsMapper.toEntity(orderDto.deliveryDetails());
            Order order = createOrder(user, itemsSet, deliveryDetails);
            Order saved = orderRepository.save(order);
            log.info("Guest order was saved: {}", saved.getId());
            return orderResponseMapper.toDTO(saved);
        } catch (Exception ex) {
            log.error("Exception occurred during creating guest order: {}", ex.getMessage());
            inventoryService.releaseInventory(productIdsWithQuantity);
            throw ex;
        }
    }

    @Override
    public OrderResponseDto createOrderFromCart(@NotNull User user, @NotNull @Valid OrderDeliveryDetailsDto orderDeliveryDetailsDto) {
        log.debug("Creating order from cart for user {}", user.getId());
        Cart cart = cartRepository.findCartByUserId(user.getId())
                .orElseThrow(() -> new UserCartNotFoundException(
                        String.format(
                                CartExceptionMP.USER_CART_WAS_NOT_FOUND, user.getId()
                        )));
        log.debug("Found user cart for user {}", user.getId());
        if (cart.getCartItems().isEmpty()) {
            log.warn("User cart is empty");
            throw new EmptyCartException(CartExceptionMP.EMPTY_USER_CART);
        }
        Set<OrderItem> orderItems = cart.getCartItems()
                .stream()
                .filter(Objects::nonNull)
                .filter(cartItem -> cartItem.getQuantity() > 0)
                .map(cartItem ->
                        OrderItem.builder()
                                .productId(cartItem.getProductId())
                                .productName(cartItem.getProductName())
                                .priceSnapshot(cartItem.getUnitPriceSnapshot())
                                .quantity(cartItem.getQuantity())
                                .build()
                ).collect(Collectors.toSet());
        Map<Long, Integer> productIdsWithQuantity = orderItems.stream()
                .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));
        try {
            inventoryService.deductInventory(productIdsWithQuantity);
            log.debug("Inventory deducted successfully for user: {}", user.getId());
            OrderDeliveryDetails deliveryDetails = orderDeliveryDetailsMapper.toEntity(orderDeliveryDetailsDto);
            Order order = createOrder(user, orderItems, deliveryDetails);
            Order saved = orderRepository.save(order);
            cart.clear();
            cartRepository.save(cart);
            return orderResponseMapper.toDTO(saved);
        } catch (Exception ex) {
            log.error("Exception occurred during creating order from cart: {}", ex.getMessage());
            inventoryService.releaseInventory(productIdsWithQuantity);
            throw ex;
        }
    }

    private Order createOrder(User user, Set<OrderItem> itemsSet,
                              OrderDeliveryDetails deliveryDetails) {
        Order order = Order.builder()
                .orderStatus(OrderStatus.CREATED)
                .user(user)
                .build();
        itemsSet.forEach(order::addItem);
        order.setOrderDeliveryDetails(deliveryDetails);
        return order;
    }
}
