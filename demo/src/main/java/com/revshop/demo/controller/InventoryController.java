package com.revshop.demo.controller;

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
    public ResponseEntity<List<InventoryItem>> getInventory(@PathVariable Long sellerId) {
        List<InventoryItem> inventoryItems = inventoryService.getInventoryItems(sellerId);
        return ResponseEntity.ok().body(inventoryItems);
    }

    @PatchMapping("/{inventoryItemId}/update-quantity")
    public ResponseEntity<InventoryItem> updateStock(@PathVariable Long inventoryItemId, @RequestBody InventoryItem inventoryItem) {
        InventoryItem updated = inventoryService.updateInventoryQuantity(inventoryItemId, inventoryItem.getQuantity());
        return ResponseEntity.ok().body(updated);
    }


}
