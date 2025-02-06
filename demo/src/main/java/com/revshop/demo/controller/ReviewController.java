package com.revshop.demo.controller;

import com.revshop.demo.dto.AddReviewDTO;
import com.revshop.demo.dto.ReviewDTO;
import com.revshop.demo.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody AddReviewDTO addReviewDTO) {
        reviewService.addReview(addReviewDTO);
        return ResponseEntity.ok("Review added successfully.");
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long productId) {
        List<ReviewDTO> reviews = reviewService.getReviewsForProduct(productId);
        return ResponseEntity.ok(reviews);
    }

    @PatchMapping("/update/{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody AddReviewDTO updatedReviewDTO) {
        reviewService.updateReview(reviewId, updatedReviewDTO);
        return ResponseEntity.ok("Review updated successfully.");
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId, @RequestParam Long reviewerId) {
        reviewService.deleteReview(reviewId, reviewerId);
        return ResponseEntity.ok("Review deleted successfully.");
    }
}
