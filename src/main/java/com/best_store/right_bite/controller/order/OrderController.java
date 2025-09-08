package com.best_store.right_bite.controller.order;

import com.best_store.right_bite.dto.order.request.OrderDeliveryDetailsDto;
import com.best_store.right_bite.dto.order.request.OrderDto;
import com.best_store.right_bite.dto.order.response.OrderResponseDto;
import com.best_store.right_bite.dto.order.response.OrdersPageableDto;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import com.best_store.right_bite.service.order.facade.OrderProcessingFacade;
import com.best_store.right_bite.service.order.finder.OrderFinderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Orders", description = "Create orders and fetch user's orders")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderProcessingFacade orderProcessingFacade;
    private final OrderFinderService orderFinderService;

    @Operation(
            summary = "Create order from current user's cart",
            description = "Creates an order using items from the authenticated user's active cart.",
            method = "POST",
            security = @SecurityRequirement(name = "JWT"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Delivery details for the order",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderDeliveryDetailsDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-from-cart")
    public ResponseEntity<OrderResponseDto> createOrderFromCart(@RequestBody @Valid @NotNull OrderDeliveryDetailsDto orderDeliveryDetailsDto,
                                                                @AuthenticationPrincipal JwtPrincipal principal) {
        OrderResponseDto orderResponseDto = orderProcessingFacade.processOrder(Long.valueOf(principal.id()), orderDeliveryDetailsDto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create order with explicit items",
            description = "Creates an order for guest or authenticated user using provided items and delivery details.",
            method = "POST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order with items and delivery details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    @PreAuthorize("permitAll()")
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid @NotNull OrderDto orderDto,
                                                        @Nullable Authentication authentication) {
        OrderResponseDto orderResponseDto = orderProcessingFacade.processOrder(orderDto, authentication);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get order by id (current user)",
            description = "Returns a single order that belongs to the authenticated user by id.",
            method = "GET",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order retrieved",
                            content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/id/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@Parameter(description = "Order identifier") @PathVariable("id") Long id,
                                                         @AuthenticationPrincipal JwtPrincipal principal) {
        OrderResponseDto orderResponseDto = orderFinderService.findUserOrderById(id, Long.parseLong(principal.id()));
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }

    @Operation(
        summary = "List current user's orders",
        description = "Returns a page of orders for the authenticated user.",
        method = "GET",
        security = @SecurityRequirement(name = "JWT"),
        responses = {
                @ApiResponse(responseCode = "200", description = "Orders page retrieved",
                        content = @Content(schema = @Schema(implementation = OrdersPageableDto.class))),
                @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                @ApiResponse(responseCode = "403", description = "Insufficient permissions")
        }
    )
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-orders")
    public ResponseEntity<OrdersPageableDto> getOrderForCurrentUSer(
            @Parameter(description = "Page number (0-based)")
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @Parameter(description = "Page size")
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @AuthenticationPrincipal JwtPrincipal principal) {
        OrdersPageableDto ordersPageableDto = orderFinderService.findAllUserOrders(
                Long.parseLong(principal.id()),
                Pageable.ofSize(pageSize).withPage(pageNo));
        return new ResponseEntity<>(ordersPageableDto, HttpStatus.OK);
    }
}
