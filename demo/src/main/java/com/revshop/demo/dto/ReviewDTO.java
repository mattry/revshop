package com.revshop.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long reviewerId;
    private String reviewText;
    private LocalDateTime reviewDate;
    private Integer rating;
}
