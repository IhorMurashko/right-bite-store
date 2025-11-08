package com.best_store.right_bite.service.order.create;

import com.best_store.right_bite.constant.constraint.order.OrderFieldConstraint;
import com.best_store.right_bite.constant.order.DeliveryMethod;
import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderDto;
import com.best_store.right_bite.dto.order.request.OrderItemDto;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.exception.exceptions.inventory.InsufficientInventoryException;
import com.best_store.right_bite.mapper.order.orderDeliveryDetails.OrderDeliveryDetailsMapper;
import com.best_store.right_bite.mapper.order.orderItem.OrderItemDtoMapper;
import com.best_store.right_bite.mapper.order.response.OrderResponseMapper;
import com.best_store.right_bite.model.order.Order;
import com.best_store.right_bite.model.order.OrderDeliveryDetails;
import com.best_store.right_bite.model.order.OrderItem;
import com.best_store.right_bite.repository.cart.CartRepository;
import com.best_store.right_bite.repository.order.OrderRepository;
import com.best_store.right_bite.service.inventory.InventoryService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderCreatorServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderItemDtoMapper orderItemDtoMapper;
    @Mock
    private OrderDeliveryDetailsMapper orderDeliveryDetailsMapper;
    @Mock
    private OrderResponseMapper orderResponseMapper;
    @Mock
    private InventoryService inventoryService;
    @InjectMocks
    private OrderCreatorServiceImpl orderCreatorService;

    private Set<OrderItemDto> orderItemDtoSet;
    private OrderDeliveryDetailsDto orderDeliveryDetailsDto;
    private OrderDto orderDto;
    private Order order;
    private Set<OrderItem> orderItems;
    private OrderDeliveryDetails orderDeliveryDetails;
    private OrderResponseDto orderResponseDto;
    private static Validator validator;


    @BeforeEach
    void setUp() {
        orderItemDtoSet = Set.of(
                new OrderItemDto(1L, 1, "Product 1", BigDecimal.valueOf(15.5)),
                new OrderItemDto(2L, 2, "Product 2", BigDecimal.valueOf(11.99)),
                new OrderItemDto(3L, 3, "Product 3", BigDecimal.valueOf(8)),
                new OrderItemDto(4L, 4, "Product 4", BigDecimal.valueOf(19.15)),
                new OrderItemDto(5L, 5, "Product 5", BigDecimal.valueOf(4.35))
        );

        orderItems = Set.of(
                new OrderItem(1L, 1L, 1, "Product 1", BigDecimal.valueOf(15.5), null),
                new OrderItem(2L, 2L, 2, "Product 1", BigDecimal.valueOf(15.5), null),
                new OrderItem(3L, 3L, 3, "Product 1", BigDecimal.valueOf(15.5), null),
                new OrderItem(4L, 4L, 4, "Product 1", BigDecimal.valueOf(15.5), null),
                new OrderItem(5L, 5L, 5, "Product 1", BigDecimal.valueOf(15.5), null)
        );
        orderDeliveryDetailsDto = new OrderDeliveryDetailsDto(
                "John",
                "Doe",
                "123456789",
                "email@email.com",
                "12",
                "street",
                "New York",
                "NY",
                "10001",
                null,
                DeliveryMethod.PICKUP
        );
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //todo:
    @Nested
    class ValidateOrderDtoTest {
        @Test
        void shouldThrowValidationConstraintViolationException_when_OrderDtoIsNull() {
            OrderDto request = new OrderDto(null, null);
            Set<ConstraintViolation<OrderDto>> violations = validator.validate(request);

            assertEquals(2, violations.size());
            Set<String> messages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            assertTrue(messages.contains(OrderFieldConstraint.NULL_ORDER_ITEMS_EXCEPTION_MESSAGE));
            assertTrue(messages.contains(OrderFieldConstraint.NULL_ORDER_DELIVERY_DETAILS_EXCEPTION_MESSAGE));

            verifyNoInteractions(orderResponseMapper, orderItemDtoMapper, orderDeliveryDetailsMapper, orderRepository,
                    cartRepository, inventoryService);
        }


    }
}