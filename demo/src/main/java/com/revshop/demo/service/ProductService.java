package com.revshop.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revshop.demo.dto.ProductDTO;
import com.revshop.demo.dto.ProductRequestDTO;
import com.revshop.demo.dto.ReviewDTO;
import com.revshop.demo.entity.Category;
import com.revshop.demo.entity.Product;
import com.revshop.demo.entity.Review;
import com.revshop.demo.entity.Seller;
import com.revshop.demo.repository.InventoryItemRepository;
import com.revshop.demo.repository.OrderItemRepository;
import com.revshop.demo.repository.ProductRepository;
import com.revshop.demo.repository.ReviewRepository;
import com.revshop.demo.repository.SellerRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final SellerRepository sellerRepository;
    private final InventoryService inventoryService;
    private final InventoryItemRepository inventoryItemRepository;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository,
            OrderItemRepository orderItemRepository, SellerRepository sellerRepository,
            InventoryService inventoryService, InventoryItemRepository inventoryItemRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.orderItemRepository = orderItemRepository;
        this.sellerRepository = sellerRepository;
        this.inventoryService = inventoryService;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setSellerId(product.getSeller().getId());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());

        List<ReviewDTO> reviews = reviewRepository.findByProductId(product.getId()).stream()
                .map(this::convertToReviewDTO)
                .collect(Collectors.toList());
        dto.setReviews(reviews);

        return dto;
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getReviewerId(),
                review.getReviewText(),
                review.getReviewDate(),
                review.getRating());
    }

    public Product addProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        Seller seller = sellerRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        product.setId(dto.getProductId()); // Usually auto-generated
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSeller(seller); // Assuming Seller is provided in DTO
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());
        return product;
    }

    public List<ProductDTO> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProduct(Long productId, Long sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own products.");
        }

        // Check if the product is in any order
        boolean productInOrder = orderItemRepository.existsByProductId(productId);
        if (productInOrder) {
            throw new RuntimeException("Cannot delete product. It is part of an active order.");
        }

        inventoryItemRepository.deleteByProduct(product);

        productRepository.delete(product);
    }

    public ProductDTO createProductForSeller(Long sellerId, ProductRequestDTO requestDTO) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new RuntimeException("Seller not found"));

        Product product = new Product();
        product.setSeller(seller);
        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setCategory(requestDTO.getCategory());
        product.setImageUrl(requestDTO.getImageUrl());

        Product saved = productRepository.save(product);

        inventoryService.addProductToInventory(seller, saved, requestDTO.getStock());

        return convertToDTO(saved);
    }

    public ProductDTO updateProduct(Long sellerId, Long productId, ProductRequestDTO requestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You can only update your own products.");
        }

        product.setName(requestDTO.getName());
        product.setDescription(requestDTO.getDescription());
        product.setPrice(requestDTO.getPrice());
        product.setCategory(requestDTO.getCategory());
        product.setImageUrl(requestDTO.getImageUrl());

        product = productRepository.save(product);

        productRepository.save(product);

        return convertToDTO(product);
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDTO(product);
    }

    public List<ProductDTO> searchProducts(String query) {
        if (query == null || query.isEmpty()) {
            throw new RuntimeException("Search query cannot be empty");
        }

        List<Product> products = productRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        List<ProductDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            dtos.add(convertToDTO(product));
        }

        return dtos;
    }

}
