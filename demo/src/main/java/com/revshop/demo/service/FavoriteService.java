package com.revshop.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revshop.demo.entity.Favorite;
import com.revshop.demo.entity.Product;
import com.revshop.demo.repository.FavoriteRepository;
import com.revshop.demo.repository.ProductRepository;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final ProductRepository productRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, ProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.productRepository = productRepository;
    }

    public void addFavorite(Long userId, Long productId) {
        if (favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new RuntimeException("Product is already in favorites.");
        }

        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favoriteRepository.save(favorite);
    }

    public List<Product> getFavorites(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
        return favorites.stream()
                .map(fav -> productRepository.findById(fav.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found")))
                .collect(Collectors.toList());
    }

    public void removeFavorite(Long userId, Long productId) {
        if (!favoriteRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new RuntimeException("Product is not in favorites.");
        }

        favoriteRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
