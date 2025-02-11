package com.revshop.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revshop.demo.dto.ProductDTO;
import com.revshop.demo.entity.Product;
import com.revshop.demo.service.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        favoriteService.addFavorite(userId, productId);
        return ResponseEntity.ok("Product added to favorites.");
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ProductDTO>> getFavorites(@PathVariable Long userId) {
        List<ProductDTO> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFavorite(@RequestParam Long userId, @RequestParam Long productId) {
        favoriteService.removeFavorite(userId, productId);
        return ResponseEntity.ok("Product removed from favorites.");
    }
}
