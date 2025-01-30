package com.revshop.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revshop.demo.dto.ProductDTO;
import com.revshop.demo.dto.ReviewDTO;
import com.revshop.demo.entity.Product;
import com.revshop.demo.entity.Review;
import com.revshop.demo.repository.OrderItemRepository;
import com.revshop.demo.repository.ProductRepository;
import com.revshop.demo.repository.ReviewRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setSeller(product.getSeller());

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
                review.getRating()
        );
    }

    public Product addProduct(ProductDTO productDTO) {
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getProductId());  // Usually auto-generated
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setSeller(dto.getSeller()); // Assuming Seller is provided in DTO
        return product;
    }

    public List<ProductDTO> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

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
    
        productRepository.delete(product);
    }
    

}
