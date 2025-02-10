package com.revshop.demo.controller;

import com.revshop.demo.dto.InventoryItemDTO;
import com.revshop.demo.entity.InventoryItem;
import com.revshop.demo.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<List<InventoryItemDTO>> getInventory(@PathVariable Long sellerId) {
        List<InventoryItemDTO> inventoryItems = inventoryService.getInventoryItems(sellerId);
        return ResponseEntity.ok().body(inventoryItems);
    }

    @PatchMapping("/{inventoryItemId}/update-quantity")
    public ResponseEntity<InventoryItemDTO> updateStock(@PathVariable Long inventoryItemId, @RequestBody InventoryItem inventoryItem) {
        InventoryItemDTO updated = inventoryService.updateInventoryQuantity(inventoryItemId, inventoryItem.getQuantity());
        return ResponseEntity.ok().body(updated);
    }

    @PatchMapping("/update-threshold")
    public ResponseEntity<String> updateThreshold(@RequestParam Long productId, @RequestParam int newThreshold) {
        inventoryService.updateThreshold(productId, newThreshold);
        return ResponseEntity.ok("Threshold updated successfully.");
    }


}
