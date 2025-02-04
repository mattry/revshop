package com.revshop.demo.service;

import com.revshop.demo.dto.AddReviewDTO;
import com.revshop.demo.dto.ReviewDTO;
import com.revshop.demo.entity.Review;
import com.revshop.demo.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void addReview(AddReviewDTO addReviewDTO) {
        Review review = new Review();
        review.setReviewerId(addReviewDTO.getReviewerId());
        review.setProductId(addReviewDTO.getProductId());
        review.setReviewText(addReviewDTO.getReviewText());
        review.setRating(addReviewDTO.getRating());
        reviewRepository.save(review);
    }

    public List<ReviewDTO> getReviewsForProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(review -> new ReviewDTO(
                        review.getId(),
                        review.getReviewerId(),
                        review.getReviewText(),
                        review.getReviewDate(),
                        review.getRating()))
                .collect(Collectors.toList());
    }

    public void updateReview(Long reviewId, AddReviewDTO updatedReviewDTO) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getReviewerId().equals(updatedReviewDTO.getReviewerId())) {
            throw new RuntimeException("Unauthorized: You can only edit your own reviews.");
        }

        review.setReviewText(updatedReviewDTO.getReviewText());
        review.setRating(updatedReviewDTO.getRating());
        reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId, Long reviewerId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getReviewerId().equals(reviewerId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own reviews.");
        }

        reviewRepository.delete(review);
    }
}
