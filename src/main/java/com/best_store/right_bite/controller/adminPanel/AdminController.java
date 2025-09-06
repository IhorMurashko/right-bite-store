package com.best_store.right_bite.controller.adminPanel;


import com.best_store.right_bite.dto.adminPanel.order.OrderDTO;
import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.service.adminPanel.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {


    private final DashboardService dashboardService;

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/admin-info")
    public ResponseEntity<DefaultUserInfoResponseDto> getAdminInfo(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getAdminInfo(authentication));
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/total-customers")
    public ResponseEntity<Long> getTotalCustomers() {
        return ResponseEntity.ok(dashboardService.getTotalCustomers());
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/total-sales")
    public ResponseEntity<BigDecimal> getTotalSales() {
        return ResponseEntity.ok(dashboardService.getTotalSales());
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/total-orders")
    public ResponseEntity<Long> getTotalOrders() {
        return ResponseEntity.ok(dashboardService.getTotalOrders());
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(dashboardService.getAllOrders());
    }

}
