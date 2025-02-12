package com.revshop.demo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revshop.demo.dto.ProductDTO;
import com.revshop.demo.dto.ProductRequestDTO;
import com.revshop.demo.entity.Category;
import com.revshop.demo.entity.Product;
import com.revshop.demo.entity.Seller;
import com.revshop.demo.repository.InventoryItemRepository;
import com.revshop.demo.repository.OrderItemRepository;
import com.revshop.demo.repository.ProductRepository;
import com.revshop.demo.repository.ReviewRepository;
import com.revshop.demo.repository.SellerRepository;
import com.revshop.demo.service.InventoryService;
import com.revshop.demo.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private InventoryService inventoryService;
    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Seller seller;
    private BigDecimal price = new BigDecimal(50.00);

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setId(1L);

        product = new Product();
        product.setId(100L);
        product.setName("Test Product");
        product.setPrice(price);
        product.setDescription("Test Description");
        product.setSeller(seller);
        product.setCategory(Category.VIDEOGAMES);
    }

    @Test
    void getAllProducts_ReturnsProductList() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<ProductDTO> products = productService.getAllProducts();
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    void addProduct_SuccessfullyAddsProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(100L);
        dto.setName("Test Product");
        dto.setPrice(price);
        dto.setDescription("Test Description");
        dto.setSellerId(1L);
        dto.setCategory(Category.VIDEOGAMES);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.addProduct(dto);
        assertNotNull(savedProduct);
        assertEquals("Test Product", savedProduct.getName());
    }

    @Test
    void getProductsBySellerId_ReturnsSellerProducts() {
        when(productRepository.findBySellerId(1L)).thenReturn(List.of(product));
        List<ProductDTO> products = productService.getProductsBySellerId(1L);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    void getProductsByCategory_ReturnsCategoryProducts() {
        when(productRepository.findByCategory(Category.VIDEOGAMES)).thenReturn(List.of(product));
        List<ProductDTO> products = productService.getProductsByCategory(Category.VIDEOGAMES);
        assertEquals(1, products.size());
        assertEquals("Test Product", products.get(0).getName());
    }

    @Test
    void deleteProduct_SuccessfullyDeletes() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(orderItemRepository.existsByProductId(100L)).thenReturn(false);

        productService.deleteProduct(100L, 1L);
        verify(inventoryItemRepository, times(1)).deleteByProduct(product);
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void updateProduct_SuccessfullyUpdates() {
        ProductRequestDTO requestDTO = new ProductRequestDTO("Updated Product", "Updated Desc", price, 4, Category.DVD_BLURAY, null);
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO updatedProduct = productService.updateProduct(1L, 100L, requestDTO);
        assertEquals("Updated Product", updatedProduct.getName());
    }

    @Test
    void getProductById_ReturnsProductDTO() {
        when(productRepository.findById(100L)).thenReturn(Optional.of(product));
        ProductDTO dto = productService.getProductById(100L);
        assertEquals("Test Product", dto.getName());
    }
}
