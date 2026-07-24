package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.dao.DAOFactory;
import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.dao.ReviewDAO;
import com.mubeen.mubeenmart.dto.ReviewRequestDTO;
import com.mubeen.mubeenmart.exception.*;
import com.mubeen.mubeenmart.model.Review;
import com.mubeen.mubeenmart.service.ReviewService;
import com.mubeen.mubeenmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for product reviews.
 * Enforces requirement F8: "Product reviews and star ratings on completed orders."
 */
public class ReviewServiceImpl implements ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewDAO reviewDAO;
    private final ProductDAO productDAO;

    public ReviewServiceImpl() {
        this(DAOFactory.getReviewDAO(), DAOFactory.getProductDAO());
    }

    public ReviewServiceImpl(ReviewDAO reviewDAO, ProductDAO productDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<Review> getReviewsForProduct(Long productId) throws AppException {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Invalid product ID");
        }
        try {
            return reviewDAO.findByProductId(productId);
        } catch (SQLException e) {
            logger.error("Database error retrieving reviews for product: {}", productId, e);
            throw new AppException("Failed to load product reviews", 500, e);
        }
    }

    @Override
    public Review submitReview(Long buyerId, ReviewRequestDTO request) throws AppException {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("User must be logged in to submit a review");
        }
        if (request == null) {
            throw new ValidationException("Review payload is required");
        }
        if (request.getProductId() == null || request.getProductId() <= 0) {
            throw new ValidationException("Product ID is required");
        }
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new ValidationException("Rating must be an integer between 1 and 5 stars");
        }
        String comment = ValidationUtil.requireNonBlank(request.getComment(), "Review comment");

        try {
            // Verify product exists
            productDAO.findById(request.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            // Requirement F8: Enforce review eligibility on completed orders
            boolean isEligible = reviewDAO.hasPurchasedAndCompleted(buyerId, request.getProductId());
            if (!isEligible) {
                throw new AuthorizationException("You can only review products from orders that have been successfully delivered/completed.");
            }

            Review review = new Review(
                    null,
                    request.getProductId(),
                    buyerId,
                    request.getRating(),
                    ValidationUtil.escapeHtml(comment),
                    LocalDateTime.now()
            );

            Review created = reviewDAO.create(review);
            logger.info("Review submitted: id={}, productId={}, buyerId={}, rating={}", created.getId(), request.getProductId(), buyerId, request.getRating());
            return created;

        } catch (SQLException e) {
            logger.error("Database error saving review for product: {}", request.getProductId(), e);
            throw new AppException("Failed to submit review", 500, e);
        }
    }

    @Override
    public boolean isEligibleToReview(Long userId, Long productId) throws AppException {
        if (userId == null || productId == null) return false;
        try {
            return reviewDAO.hasPurchasedAndCompleted(userId, productId);
        } catch (SQLException e) {
            logger.error("Database error checking review eligibility", e);
            return false;
        }
    }
}
