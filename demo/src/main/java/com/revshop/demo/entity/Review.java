package com.revshop.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long reviewerId;
    private Long productId;
    private String reviewText;

    @CreationTimestamp
    private LocalDateTime reviewDate;

    @Range(min = 1, max = 5)
    private Integer rating;

}
