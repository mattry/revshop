package com.revshop.demo.repository;

import com.revshop.demo.entity.InventoryItem;
import com.revshop.demo.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    void deleteByProduct(Product product);
}
