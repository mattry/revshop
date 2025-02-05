package com.revshop.demo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.revshop.demo.dto.CartRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.revshop.demo.dto.CartDTO;
import com.revshop.demo.dto.CartItemDTO;
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

    public void addToCart(CartRequestDTO cartRequestDTO) {
        Cart cart = cartRepository.findByBuyerId(cartRequestDTO.getBuyerId())
                .orElseGet(() -> createNewCart(cartRequestDTO.getBuyerId()));

        Product product = productRepository.findById(cartRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + cartRequestDTO.getQuantity());
            cartItemRepository.save(existingItem.get());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartRequestDTO.getQuantity());
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
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

    public CartDTO getCartDetails(Long buyerId) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new RuntimeException("Cart not found for buyer"));

        List<CartItemDTO> items = cart.getCartItems().stream()
                .map(item -> new CartItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity(),
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartDTO(cart.getId(), cart.getBuyer().getId(), items, total);
    }

    public List<CartItem> getCartItems(Long buyerId) {
        Cart cart = cartRepository.findByBuyerId(buyerId)
                .orElseThrow(() -> new RuntimeException("Cart not found for buyer"));

        return cart.getCartItems();
    }

    public void clearCart(Buyer buyer) {
        Cart cart = cartRepository.findByBuyerId(buyer.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found for buyer"));
        cart.getCartItems().clear();
    }
}
