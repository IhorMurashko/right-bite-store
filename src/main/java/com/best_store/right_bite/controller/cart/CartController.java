package com.best_store.right_bite.controller.cart;

import com.best_store.right_bite.dto.cart.request.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.remove.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.service.cart.application.CartFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "User cart controller",
        description = "get, add, update, remove, clear operations with user's cart. " +
                "Each request must contains JWT token in the headers." +
                "Only users with role USER could user this endpoints."
)
@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartFacade cartFacade;

    @Operation(
            summary = "Get current user's cart",
            description = "Returns the active cart for the authenticated user (derived from the JWT).",
            method = "GET",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @GetMapping
    public ResponseEntity<CartResponseDto> getUserCart(Authentication authentication) {
        CartResponseDto userCart = cartFacade.getUserCart(authentication);
        return new ResponseEntity<>(userCart, HttpStatus.OK);
    }

    @Operation(
            summary = "Add items to cart",
            description = "Adds one or more unique items to the authenticated user's cart. User and cart are resolved from the JWT.",
            method = "POST",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Items to add to the cart",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddCartRequestDto.class))),
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Items added successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Validation or request error"),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addCartItem(@RequestBody AddCartRequestDto cartDto,
                                                       Authentication authentication) {
        CartResponseDto cartResponseDto = cartFacade.addItems(cartDto, authentication);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Remove items from cart",
            description = "Removes selected items from the authenticated user's cart.",
            method = "DELETE",
            security = @SecurityRequirement(name = "JWT"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Identifiers of items to remove from the cart",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RemoveItemsRequestDto.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Items removed successfully",
                            content = @Content(schema = @Schema(implementation = CartResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error"),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @DeleteMapping("/delete")
    public ResponseEntity<CartResponseDto> deleteCartItem(@RequestBody RemoveItemsRequestDto removeItemsRequestDto,
                                                          Authentication authentication) {
        CartResponseDto cartResponseDto = cartFacade.removeItems(removeItemsRequestDto, authentication);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.OK);

    }

    @Operation(
            summary = "Clear cart",
            description = "Removes all items from the authenticated user's cart.",
            method = "DELETE",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cart cleared (no content)"),
                    @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions")
            }
    )
    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @DeleteMapping("/clear")
    public ResponseEntity<HttpStatus> clearUserCart(Authentication authentication) {
        cartFacade.clear(authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
