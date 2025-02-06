package com.revshop.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    // BuyerOrder or SellerOrder ID
    private Long orderId;
    private List<OrderItemDTO> items;
    private BigDecimal total;
}
