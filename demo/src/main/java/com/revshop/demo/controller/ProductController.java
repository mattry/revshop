package com.revshop.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.demo.dto.ProductDTO;
import com.revshop.demo.entity.Product;
import com.revshop.demo.service.ProductService;



@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/getProducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> dtos = productService.getAllProducts();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
        Product savedProduct = productService.addProduct(productDTO);
        return ResponseEntity.ok(savedProduct);
    }
    
    @GetMapping("/getBySeller/{sellerId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySellerId(@PathVariable Long sellerId) {
        List<ProductDTO> dtos = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId, @RequestParam Long sellerId) {
        productService.deleteProduct(productId, sellerId);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long productId) {
        ProductDTO dto = productService.getProductById(productId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String keyword) {
        List<ProductDTO> dtos = productService.searchProducts(keyword);
        return ResponseEntity.ok(dtos);
    }

}
