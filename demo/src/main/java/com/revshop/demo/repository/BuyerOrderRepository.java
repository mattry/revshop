package com.revshop.demo.repository;

import com.revshop.demo.entity.BuyerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyerOrderRepository extends JpaRepository<BuyerOrder, Long> {

    List<BuyerOrder> findAllByBuyerId(Long buyerId);

}
