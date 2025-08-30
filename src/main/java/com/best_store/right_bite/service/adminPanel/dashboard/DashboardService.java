package com.best_store.right_bite.service.adminPanel.dashboard;

import com.best_store.right_bite.dto.adminPanel.AdminInfoDTO;
import com.best_store.right_bite.dto.orders.OrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    ResponseEntity<AdminInfoDTO> getAdminInfo(Authentication authentication);

    ResponseEntity<Long> getTotalCustomers();
    ResponseEntity<BigDecimal> getTotalSales();
    ResponseEntity<Long> getTotalOrders();

    ResponseEntity<List<OrderDTO>> getAllOrders();
}
