package com.best_store.right_bite.service.adminPanel.dashboard;

import com.best_store.right_bite.dto.adminPanel.AdminInfoDTO;
import com.best_store.right_bite.dto.orders.OrderDTO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardsServiceImpl implements DashboardService{


    @Override
    public ResponseEntity<AdminInfoDTO> getAdminInfo(Authentication authentication) {
        return null;
    }

    @Override
    public ResponseEntity<Long> getTotalCustomers() {
        return null;
    }

    @Override
    public ResponseEntity<BigDecimal> getTotalSales() {
        return null;
    }

    @Override
    public ResponseEntity<Long> getTotalOrders() {
        return null;
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return null;
    }
}
