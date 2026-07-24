package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.dto.ReviewRequestDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Review;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.ReviewService;
import com.mubeen.mubeenmart.service.impl.ReviewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller servlet for submitting reviews and star ratings.
 * Fulfills Requirement F8: "Product reviews and star ratings on completed orders."
 */
@WebServlet("/reviews/*")
public class ReviewServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(ReviewServlet.class);
    private final ReviewService reviewService;

    public ReviewServlet() {
        this(new ReviewServiceImpl());
    }

    public ReviewServlet(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Please log in to submit a review"));
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        Long productId;
        int rating;
        String comment;

        if (isJsonRequest(request)) {
            ReviewRequestDTO dto = readJsonBody(request, ReviewRequestDTO.class);
            productId = dto != null ? dto.getProductId() : null;
            rating = dto != null && dto.getRating() != null ? dto.getRating() : 0;
            comment = dto != null ? dto.getComment() : null;
        } else {
            productId = Long.parseLong(request.getParameter("productId"));
            rating = Integer.parseInt(request.getParameter("rating"));
            comment = request.getParameter("comment");
        }

        try {
            ReviewRequestDTO dto = new ReviewRequestDTO(productId, rating, comment);
            Review createdReview = reviewService.submitReview(user.getId(), dto);

            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_CREATED, ApiResponse.success(createdReview));
            } else {
                response.sendRedirect(request.getContextPath() + "/products/" + productId + "?reviewAdded=true");
            }
        } catch (AppException e) {
            logger.warn("Review submission rejected for user {}: {}", user.getId(), e.getMessage());
            if (isJsonRequest(request)) {
                sendJsonResponse(response, e.getStatusCode(), ApiResponse.error("REVIEW_ERROR", e.getMessage()));
            } else {
                response.sendRedirect(request.getContextPath() + "/products/" + productId + "?reviewError=" +
                        java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
            }
        }
    }
}
