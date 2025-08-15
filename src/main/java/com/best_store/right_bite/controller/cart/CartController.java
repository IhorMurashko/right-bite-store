package com.best_store.right_bite.controller.cart;

import com.best_store.right_bite.dto.cart.request.AddCartRequestDto;
import com.best_store.right_bite.dto.cart.request.remove.RemoveItemsRequestDto;
import com.best_store.right_bite.dto.cart.response.CartResponseDto;
import com.best_store.right_bite.service.cart.application.CartFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartFacade cartFacade;

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @GetMapping
    public ResponseEntity<CartResponseDto> getUserCart(Authentication authentication) {
        CartResponseDto userCart = cartFacade.getUserCart(authentication);
        return new ResponseEntity<>(userCart, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addCartItem(@RequestBody AddCartRequestDto cartDto,
                                                       Authentication authentication) {
        CartResponseDto cartResponseDto = cartFacade.addItems(cartDto, authentication);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @DeleteMapping("/delete")
    public ResponseEntity<CartResponseDto> deleteCartItem(@RequestBody RemoveItemsRequestDto removeItemsRequestDto,
                                                          Authentication authentication) {
        CartResponseDto cartResponseDto = cartFacade.removeItems(removeItemsRequestDto, authentication);
        return new ResponseEntity<>(cartResponseDto, HttpStatus.OK);

    }

    @PreAuthorize("hasRole(T(com.best_store.right_bite.model.role.RoleName).ROLE_USER.name())")
    @DeleteMapping("/clear")
    public ResponseEntity<HttpStatus> clearUserCart(Authentication authentication) {
        cartFacade.clear(authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
