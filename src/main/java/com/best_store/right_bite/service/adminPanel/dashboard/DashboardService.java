package com.best_store.right_bite.service.adminPanel.dashboard;

import com.best_store.right_bite.dto.adminPanel.order.OrderDTO;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    DefaultUserInfoResponseDto getAdminInfo(Authentication authentication);

    Long getTotalCustomers();
    BigDecimal getTotalSales();
    Long getTotalOrders();

    List<OrderDTO> getAllOrders();
}
