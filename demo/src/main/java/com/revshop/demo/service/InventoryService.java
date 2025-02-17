package com.revshop.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revshop.demo.dto.InventoryItemDTO;
import com.revshop.demo.entity.Inventory;
import com.revshop.demo.entity.InventoryItem;
import com.revshop.demo.entity.Product;
import com.revshop.demo.entity.Seller;
import com.revshop.demo.repository.InventoryItemRepository;
import com.revshop.demo.repository.InventoryRepository;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final EmailService emailService;

    public InventoryService(InventoryRepository inventoryRepository, InventoryItemRepository inventoryItemRepository,
            EmailService emailService) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.emailService = emailService;
    }

    private Inventory createInventory(Seller seller) {
        Inventory inventory = new Inventory();
        inventory.setSeller(seller);
        return inventoryRepository.save(inventory);
    }

    public Inventory getOrCreateInventory(Seller seller) {
        return inventoryRepository.findBySellerId(seller.getId())
                .orElseGet(() -> createInventory(seller));
    }

    public List<InventoryItemDTO> getInventoryItems(Long sellerId) {
        Inventory inventory = inventoryRepository.findBySellerId(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for seller"));
        return inventory.getInventoryItems().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void addProductToInventory(Seller seller, Product product, int quantity) {
        Inventory inventory = getOrCreateInventory(seller);

        InventoryItem item = new InventoryItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setInventory(inventory);

        item.setThreshold((int) Math.ceil(quantity * 0.10));

        inventoryItemRepository.save(item);
    }

    public InventoryItemDTO updateInventoryQuantity(Long inventoryItemId, int quantity) {

        InventoryItem item = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setQuantity(quantity);

        return mapToDTO(inventoryItemRepository.save(item));
    }

    public InventoryItemDTO mapToDTO(InventoryItem inventoryItem) {
        InventoryItemDTO inventoryItemDTO = new InventoryItemDTO();
        inventoryItemDTO.setId(inventoryItem.getId());
        inventoryItemDTO.setProductName(inventoryItem.getProduct().getName());
        inventoryItemDTO.setQuantity(inventoryItem.getQuantity());
        inventoryItemDTO.setImageUrl(inventoryItem.getProduct().getImageUrl());
        inventoryItemDTO.setThreshold(inventoryItem.getThreshold());

        return inventoryItemDTO;
    }

    public boolean decrementInventory(Long productId, int quantity) {
        InventoryItem item = inventoryItemRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product inventory not found"));

        int remainingStock = item.getQuantity() - quantity;

        // 🔹 Prevent orders if stock is below zero
        if (remainingStock < 0) {
            return false; // Not enough stock
        }

        // 🔹 Update inventory
        item.setQuantity(remainingStock);
        inventoryItemRepository.save(item);

        if (remainingStock < item.getThreshold()) {
            System.out.println("Warning: Stock for product " + item.getProduct().getName() + " is below threshold ("
                    + item.getThreshold() + ")");
            sendLowStockEmail(item);
        }

        return true;
    }

    public void updateThreshold(Long productId, int newThreshold) {
        InventoryItem item = inventoryItemRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product inventory not found"));

        if (newThreshold < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative.");
        }

        item.setThreshold(newThreshold);
        inventoryItemRepository.save(item);
    }

    private void sendLowStockEmail(InventoryItem item) {
        String sellerEmail = item.getInventory().getSeller().getEmail();
        String subject = "Low Stock Alert for Product: " + item.getProduct().getName();
        String message = "Dear " + item.getInventory().getSeller().getUsername() + ",\n\n"
                + "This is to notify you that the stock for your product, "
                + item.getProduct().getName() + ", has fallen below the configured threshold of "
                + item.getThreshold() + " units.\n"
                + "Current stock: " + item.getQuantity() + " units.\n\n"
                + "Please restock soon to avoid order cancellations.\n\n"
                + "Thank you,\nRevShop Team";

        emailService.sendEmail(sellerEmail, subject, message);
    }

}
