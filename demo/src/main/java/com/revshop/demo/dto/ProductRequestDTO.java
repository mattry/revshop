package com.revshop.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal price;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int stock;

}
