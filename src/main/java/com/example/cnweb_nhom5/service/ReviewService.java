package com.example.cnweb_nhom5.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cnweb_nhom5.domain.Review;
import com.example.cnweb_nhom5.repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    // public void saveReview(Review review) {
    //     reviewRepository.save(review);
    // }
    public Review createReview(Review rv) {
        return this.reviewRepository.save(rv);
    }
}