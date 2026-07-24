package com.mubeen.mubeenmart.dao.impl;

import com.mubeen.mubeenmart.dao.ReviewDAO;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link ReviewDAO} using PreparedStatement and try-with-resources.
 */
public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public List<Review> findByProductId(Long productId) throws SQLException {
        String sql = "SELECT r.id, r.product_id, r.user_id, r.rating, r.comment, r.created_at, u.name AS user_name " +
                     "FROM reviews r JOIN users u ON r.user_id = u.id " +
                     "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        List<Review> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToReview(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean hasPurchasedAndCompleted(Long userId, Long productId) throws SQLException {
        String sql = "SELECT 1 FROM orders o " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.buyer_id = ? AND oi.product_id = ? AND o.status IN ('DELIVERED', 'COMPLETED')";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Review create(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?)";
        Timestamp now = Timestamp.valueOf(review.getCreatedAt() != null ? review.getCreatedAt() : java.time.LocalDateTime.now());
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, review.getProductId());
            ps.setLong(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, now);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getLong(1));
                }
            }
        }
        review.setCreatedAt(now.toLocalDateTime());
        return review;
    }

    @Override
    public double getAverageRating(Long productId) throws SQLException {
        String sql = "SELECT COALESCE(AVG(rating), 0.0) FROM reviews WHERE product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    @Override
    public int getReviewCount(Long productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reviews WHERE product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        Review review = new Review(
                rs.getLong("id"),
                rs.getLong("product_id"),
                rs.getLong("user_id"),
                rs.getInt("rating"),
                rs.getString("comment"),
                ts != null ? ts.toLocalDateTime() : null
        );
        review.setUserName(rs.getString("user_name"));
        return review;
    }
}
