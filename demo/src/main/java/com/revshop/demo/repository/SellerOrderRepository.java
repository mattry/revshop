package com.revshop.demo.repository;

import com.revshop.demo.entity.SellerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellerOrderRepository extends JpaRepository<SellerOrder, Long> {

    List<SellerOrder> findAllBySellerId(Long sellerId);
}
