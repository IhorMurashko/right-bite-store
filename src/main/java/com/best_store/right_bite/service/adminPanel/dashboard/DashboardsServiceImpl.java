package com.best_store.right_bite.service.adminPanel.dashboard;

import com.best_store.right_bite.dto.adminPanel.order.OrderDTO;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.mapper.adminPanel.OrderMapper;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import com.best_store.right_bite.service.order.domain.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

//TODO add description code here
public class DashboardsServiceImpl implements DashboardService{

    private final OrderService orderService;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;
    private final DefaultUserInfoDtoMapper defaultUserInfoDtoMapper;


    @Override
    public DefaultUserInfoResponseDto getAdminInfo(Authentication authentication) {
        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();
        long userId = Long.parseLong(principal.id());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id " + userId));

        return defaultUserInfoDtoMapper.toDTO(user);
    }

    @Override
    public Long getTotalCustomers() {
        return userRepository.countUsers();
    }

    @Override
    public BigDecimal getTotalSales() {
        return orderService.totalPriceOrders();
    }

    @Override
    public Long getTotalOrders() {
        return orderService.totalCountOrders();
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        log.info("getting all orders");
        return orderService.findAll().stream().map(orderMapper::toDTO).toList();
    }
}
