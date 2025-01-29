package com.revshop.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import com.revshop.demo.entity.Seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;

    private Seller seller;

    private String name;

    private String description;

    private BigDecimal price;

    private List<ReviewDTO> reviews;
}
