package com.revshop.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import com.revshop.demo.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;

    private Long sellerId;

    private String name;

    private String description;

    private BigDecimal price;

    private List<ReviewDTO> reviews;

    private Category category;
}
