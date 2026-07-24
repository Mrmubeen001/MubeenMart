package com.mubeen.mubeenmart.dao.impl;

import com.mubeen.mubeenmart.dao.WishlistDAO;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.model.WishlistItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link WishlistDAO} using PreparedStatement and try-with-resources.
 */
public class WishlistDAOImpl implements WishlistDAO {

    @Override
    public List<WishlistItem> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT w.id, w.user_id, w.product_id, w.created_at, " +
                     "p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.seller_id " +
                     "FROM wishlist_items w " +
                     "JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? ORDER BY w.created_at DESC";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToWishlistItem(rs));
                }
            }
        }
        return list;
    }

    @Override
    public WishlistItem add(Long userId, Long productId) throws SQLException {
        if (isInWishlist(userId, productId)) {
            // Already present, return existing
            List<WishlistItem> list = findByUserId(userId);
            for (WishlistItem item : list) {
                if (item.getProductId().equals(productId)) {
                    return item;
                }
            }
        }

        String sql = "INSERT INTO wishlist_items (user_id, product_id, created_at) VALUES (?, ?, ?)";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        WishlistItem newItem = new WishlistItem(null, userId, productId, now.toLocalDateTime());
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setTimestamp(3, now);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    newItem.setId(rs.getLong(1));
                }
            }
        }
        return newItem;
    }

    @Override
    public boolean remove(Long userId, Long productId) throws SQLException {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) throws SQLException {
        String sql = "SELECT 1 FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private WishlistItem mapResultSetToWishlistItem(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        WishlistItem item = new WishlistItem(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("product_id"),
                ts != null ? ts.toLocalDateTime() : null
        );

        Product p = new Product();
        p.setId(rs.getLong("product_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setSellerId(rs.getLong("seller_id"));
        item.setProduct(p);

        return item;
    }
}
