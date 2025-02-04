package com.revshop.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewDTO {
    private Long reviewerId;
    private Long productId;
    private String reviewText;
    private Integer rating;
}
