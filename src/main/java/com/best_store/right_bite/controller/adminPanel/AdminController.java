package com.best_store.right_bite.controller.adminPanel;


import com.best_store.right_bite.dto.adminPanel.AdminInfoDTO;
import com.best_store.right_bite.dto.orders.OrderDTO;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {


    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/admin-info")
    public ResponseEntity<AdminInfoDTO> getAdminInfo(Authentication authentication) {
        JwtPrincipal principal = (JwtPrincipal) authentication.getPrincipal();

        String adminId = principal.id();
        String adminEmail = principal.email();

        //TODO need get more info about admin
        AdminInfoDTO adminInfo = new AdminInfoDTO(adminId, adminEmail);
        return ResponseEntity.ok(adminInfo);
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/total-customers")
    public ResponseEntity<Long> getTotalCustomers() {
        return ResponseEntity.ok(2322L);
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/total-sales")
    public ResponseEntity<BigDecimal> getTotalSales() {
        return ResponseEntity.ok(BigDecimal.valueOf(231.3));
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/total-orders")
    public ResponseEntity<Long> getTotalOrders() {
        return ResponseEntity.ok(123L);
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_ADMIN.name())")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = new ArrayList<>();
        orders.add(
                OrderDTO.builder()
                        .id(1L)
                        .id_customer(1L)
                        .numberOrder("Test")
                        .build()
        );
        orders.add(
                OrderDTO.builder()
                        .id(2L)
                        .id_customer(2L)
                        .numberOrder("Test2")
                        .build()
        );

        return ResponseEntity.ok(orders);
    }

}
