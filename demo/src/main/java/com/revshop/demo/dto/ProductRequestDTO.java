package com.revshop.demo.dto;

import com.revshop.demo.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/*
    DTO meant to represent incoming requests to create products or update products
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;

}
