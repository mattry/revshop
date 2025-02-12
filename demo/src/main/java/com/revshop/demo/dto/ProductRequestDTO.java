package com.revshop.demo.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.revshop.demo.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Category category;

    private String imageUrl;

}
