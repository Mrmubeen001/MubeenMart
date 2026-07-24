package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.dao.impl.*;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReviewDAO Tests")
class ReviewDAOTest extends BaseDAOTest {

    private ReviewDAO reviewDAO;
    private Long buyerId;
    private Long productId;

    @BeforeEach
    void setUp() throws SQLException {
        reviewDAO = new ReviewDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();
        OrderDAO orderDAO = new OrderDAOImpl();
        OrderItemDAO orderItemDAO = new OrderItemDAOImpl();

        String sellerEmail = "rev_seller_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User seller = userDAO.create(new User(null, "Review Seller", sellerEmail, "hash", Role.SELLER, LocalDateTime.now()));

        String buyerEmail = "rev_buyer_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User buyer = userDAO.create(new User(null, "Review Buyer", buyerEmail, "hash", Role.BUYER, LocalDateTime.now()));
        buyerId = buyer.getId();

        Product product = productDAO.create(new Product(
                null, seller.getId(), "Reviewable Item", "Desc",
                new BigDecimal("60.00"), 10, "Books", "img", LocalDateTime.now()
        ));
        productId = product.getId();

        // Create completed/delivered order for buyer
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            Order order = orderDAO.create(conn, new Order(null, buyerId, OrderStatus.DELIVERED, new BigDecimal("60.00"), LocalDateTime.now()));
            orderItemDAO.create(conn, new OrderItem(null, order.getId(), productId, 1, new BigDecimal("60.00"), LocalDateTime.now()));
        }
    }

    @Test
    @DisplayName("Should verify purchase eligibility, insert review, and compute average rating")
    void testReviewWorkflow() throws SQLException {
        // 1. Verify eligibility
        assertTrue(reviewDAO.hasPurchasedAndCompleted(buyerId, productId));

        // 2. Insert Review
        Review review = new Review(null, productId, buyerId, 5, "Excellent study material!", LocalDateTime.now());
        Review created = reviewDAO.create(review);
        assertNotNull(created.getId());

        // 3. Query Reviews
        List<Review> reviews = reviewDAO.findByProductId(productId);
        assertEquals(1, reviews.size());
        assertEquals(5, reviews.get(0).getRating());

        // 4. Compute Rating & Count
        assertEquals(5.0, reviewDAO.getAverageRating(productId));
        assertEquals(1, reviewDAO.getReviewCount(productId));
    }
}
