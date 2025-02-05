package com.revshop.demo.repository;

import com.revshop.demo.entity.SellerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerOrderRepository extends JpaRepository<SellerOrder, Long> {

}
