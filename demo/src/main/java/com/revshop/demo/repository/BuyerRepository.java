package com.revshop.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revshop.demo.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, Long>{

}
