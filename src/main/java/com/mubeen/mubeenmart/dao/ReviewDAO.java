package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.Review;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for reviews and ratings.
 */
public interface ReviewDAO {

    /**
     * Finds all reviews for a product.
     *
     * @param productId product ID
     * @return list of reviews
     * @throws SQLException on database error
     */
    List<Review> findByProductId(Long productId) throws SQLException;

    /**
     * Checks if a buyer has purchased a specific product in a completed/delivered order.
     * Required for F8: "Product reviews and star ratings on completed orders."
     *
     * @param userId user ID
     * @param productId product ID
     * @return true if eligible to review
     * @throws SQLException on database error
     */
    boolean hasPurchasedAndCompleted(Long userId, Long productId) throws SQLException;

    /**
     * Inserts a new review.
     *
     * @param review review to insert
     * @return created review with generated ID
     * @throws SQLException on database error
     */
    Review create(Review review) throws SQLException;

    /**
     * Computes the average star rating for a product.
     *
     * @param productId product ID
     * @return average rating (1.0 - 5.0) or 0.0 if no reviews
     * @throws SQLException on database error
     */
    double getAverageRating(Long productId) throws SQLException;

    /**
     * Counts the number of reviews for a product.
     *
     * @param productId product ID
     * @return count of reviews
     * @throws SQLException on database error
     */
    int getReviewCount(Long productId) throws SQLException;
}
