package com.best_store.right_bite.service.order.facade;

import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderDto;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.service.order.create.OrderCreatorService;
import com.best_store.right_bite.utils.security.AuthenticationParserUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingFacadeImpl implements OrderProcessingFacade {

    private final OrderCreatorService orderCreatorService;
    private final UserRepository userRepository;
    private final AuthenticationParserUtil authenticationParserUtil;

    @Override
    public OrderResponseDto processOrder(@NotNull @Valid OrderDto orderDto, @Nullable Authentication authentication) {
        User user = null;
        if (authentication != null) {
            log.info("Authentication found, processing order for user");
            Long userId = authenticationParserUtil.extractUserLongIdFromAuthentication(authentication);
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(String.format(
                            ExceptionMessageProvider.USER_ID_NOT_FOUND, userId)));
        }
        return orderCreatorService.createGuestOrder(orderDto, user);
    }

    @Override
    public OrderResponseDto processOrder(@NotNull Long userId, @NotNull @Valid OrderDeliveryDetailsDto orderDeliveryDetailsDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(
                        ExceptionMessageProvider.USER_ID_NOT_FOUND, userId)));
        return orderCreatorService.createOrderFromCart(user, orderDeliveryDetailsDto);
    }
}
