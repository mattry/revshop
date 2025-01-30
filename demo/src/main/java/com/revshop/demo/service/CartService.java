package com.revshop.demo.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revshop.demo.entity.Buyer;
import com.revshop.demo.entity.Cart;
import com.revshop.demo.entity.CartItem;
import com.revshop.demo.entity.Product;
import com.revshop.demo.repository.BuyerRepository;
import com.revshop.demo.repository.CartItemRepository;
import com.revshop.demo.repository.CartRepository;
import com.revshop.demo.repository.ProductRepository;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final BuyerRepository buyerRepository; 

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, BuyerRepository buyerRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.buyerRepository = buyerRepository;
    }

    public void addToCart(Long buyerId, Long productId, int quantity) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseGet(() -> createNewCart(buyerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
            cartItemRepository.save(existingItem.get());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }
    }

    public void removeFromCart(Long buyerId, Long productId) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
    }

    private Cart createNewCart(Long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));

        Cart cart = new Cart();
        cart.setBuyer(buyer);
        cart.setCartItems(new ArrayList<>());
        return cartRepository.save(cart);
    }
}
