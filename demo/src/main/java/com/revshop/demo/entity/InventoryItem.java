package com.revshop.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private Inventory inventory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private int threshold;
    
    @PrePersist
    public void setDefaultThreshold() {
        if (threshold == 0) { // Default to 10% of initial stock
            threshold = (int) Math.ceil(quantity * 0.10);
        }
    }  

}
