package com.revshop.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.demo.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long buyerId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(buyerId, productId, quantity);
        return ResponseEntity.ok("Product added to cart.");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long buyerId, @RequestParam Long productId) {
        cartService.removeFromCart(buyerId, productId);
        return ResponseEntity.ok("Product removed from cart.");
    }
}
