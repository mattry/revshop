package com.revshop.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Seller extends User {

    @OneToMany(mappedBy = "seller")
    private List<Product> products;

    @OneToMany(mappedBy = "seller")
    private List<SellerOrder> orders;

    @Column
    private String businessName;

    public Seller() {
        super();
    }
}
