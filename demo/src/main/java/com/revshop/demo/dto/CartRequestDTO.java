package com.revshop.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequestDTO {
    private Long buyerId;
    private Long productId;
    private int quantity;
}
