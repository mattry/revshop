package com.revshop.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.demo.dto.CartDTO;
import com.revshop.demo.dto.CartRequestDTO;
import com.revshop.demo.service.CartService;
import com.revshop.demo.service.UserService;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    private Long getUserId(Authentication auth) {
        String username = auth.getName();
        return userService.getUserByUsername(username).getId();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartRequestDTO cartRequestDTO) {
        cartService.addToCart(cartRequestDTO);
        return ResponseEntity.ok("Product added to cart.");
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long productId, Authentication auth) {
        cartService.removeFromCart(getUserId(auth), productId);
        return ResponseEntity.ok("Product removed from cart.");
    }

    @GetMapping("/details/{buyerId}")
    public ResponseEntity<CartDTO> getCartDetails(@PathVariable Long buyerId) {
        CartDTO cartDetails = cartService.getCartDetails(buyerId);
        return ResponseEntity.ok(cartDetails);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateCartItem(@RequestParam Long buyerId, @RequestParam Long productId,
            @RequestParam int quantity) {
        cartService.updateCartItem(buyerId, productId, quantity);
        return ResponseEntity.ok("Cart updated successfully.");
    }
}
