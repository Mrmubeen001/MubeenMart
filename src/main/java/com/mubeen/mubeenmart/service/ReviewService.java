package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dto.ReviewRequestDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Review;

import java.util.List;

/**
 * Service interface for customer product reviews and star ratings.
 */
public interface ReviewService {

    /**
     * Retrieves all verified customer reviews for a product.
     *
     * @param productId product ID
     * @return list of reviews
     * @throws AppException on failure
     */
    List<Review> getReviewsForProduct(Long productId) throws AppException;

    /**
     * Submits a customer review for a product.
     * Fulfills Requirement F8: verifies that the buyer has completed an order for this product.
     *
     * @param buyerId buyer user ID
     * @param request review payload (rating, comment)
     * @return created Review
     * @throws AppException if validation fails or buyer has not completed purchase
     */
    Review submitReview(Long buyerId, ReviewRequestDTO request) throws AppException;

    /**
     * Checks if a user is eligible to review a product.
     *
     * @param userId buyer user ID
     * @param productId product ID
     * @return true if buyer completed purchase
     * @throws AppException on failure
     */
    boolean isEligibleToReview(Long userId, Long productId) throws AppException;
}
