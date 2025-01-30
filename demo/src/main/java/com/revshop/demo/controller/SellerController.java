package com.revshop.demo.controller;

import com.revshop.demo.dto.ProductDTO;
import com.revshop.demo.dto.ProductRequestDTO;
import com.revshop.demo.entity.Product;
import com.revshop.demo.service.InventoryService;
import com.revshop.demo.service.ProductService;
import com.revshop.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final ProductService productService;
    private final InventoryService inventoryService;
    private final UserService userService;

    public SellerController(ProductService productService, InventoryService inventoryService, UserService userService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.userService = userService;
    }

    private Long getUserId(Authentication auth) {
        String username = auth.getName();
        return userService.getUserByUsername(username).getId();
    }

    @PostMapping("/{sellerId}/products")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long sellerId, @RequestBody ProductRequestDTO request) {
        ProductDTO product = productService.createProductForSeller(sellerId, request);
        return ResponseEntity.ok(product);
    }

    @PatchMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(Authentication auth, @PathVariable Long productId, @RequestBody ProductRequestDTO request) {
        Long userId = getUserId(auth);
        ProductDTO updated = productService.updateProduct(userId, productId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(Authentication auth, @PathVariable Long productId) {
        productService.deleteProduct(productId, getUserId(auth));
        return ResponseEntity.noContent().build();
    }


}
