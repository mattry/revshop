package com.revshop.demo.service;

import com.revshop.demo.dto.InventoryItemDTO;
import com.revshop.demo.entity.Inventory;
import com.revshop.demo.entity.InventoryItem;
import com.revshop.demo.entity.Product;
import com.revshop.demo.entity.Seller;
import com.revshop.demo.repository.InventoryItemRepository;
import com.revshop.demo.repository.InventoryRepository;
import com.revshop.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryService(InventoryRepository inventoryRepository, InventoryItemRepository inventoryItemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryItemRepository = inventoryItemRepository;
    }

    private Inventory createInventory(Seller seller){
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
        return inventory.getInventoryItems().stream().map(item ->
                mapToDTO(item)).collect(Collectors.toList());
    }

    public void addProductToInventory(Seller seller, Product product, int quantity) {
        Inventory inventory = getOrCreateInventory(seller);

        InventoryItem item = new InventoryItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setInventory(inventory);

        inventoryItemRepository.save(item);
    }

    public InventoryItemDTO updateInventoryQuantity(Long inventoryItemId, int quantity) {

        InventoryItem item = inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(()-> new RuntimeException("Item not found"));

        item.setQuantity(quantity);

        return mapToDTO(inventoryItemRepository.save(item));
    }

    public InventoryItemDTO mapToDTO (InventoryItem inventoryItem) {
        InventoryItemDTO inventoryItemDTO = new InventoryItemDTO();
        inventoryItemDTO.setId(inventoryItem.getId());
        inventoryItemDTO.setProductName(inventoryItem.getProduct().getName());
        inventoryItemDTO.setQuantity(inventoryItem.getQuantity());

        return inventoryItemDTO;
    }
}
