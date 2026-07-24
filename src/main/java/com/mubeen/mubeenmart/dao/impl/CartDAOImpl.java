package com.mubeen.mubeenmart.dao.impl;

import com.mubeen.mubeenmart.dao.CartDAO;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.CartItem;
import com.mubeen.mubeenmart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of {@link CartDAO} using PreparedStatement and try-with-resources.
 */
public class CartDAOImpl implements CartDAO {

    @Override
    public List<CartItem> findByUserId(Long userId) throws SQLException {
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, c.created_at, " +
                     "p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.seller_id " +
                     "FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ? ORDER BY c.id ASC";
        List<CartItem> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToCartItem(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Optional<CartItem> findByUserAndProduct(Long userId, Long productId) throws SQLException {
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, c.created_at, " +
                     "p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.seller_id " +
                     "FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ? AND c.product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCartItem(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public CartItem addOrUpdateItem(Long userId, Long productId, int quantity) throws SQLException {
        Optional<CartItem> existing = findByUserAndProduct(userId, productId);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            int newQuantity = item.getQuantity() + quantity;
            updateQuantity(item.getId(), newQuantity);
            item.setQuantity(newQuantity);
            return item;
        } else {
            String sql = "INSERT INTO cart_items (user_id, product_id, quantity, created_at) VALUES (?, ?, ?, ?)";
            Timestamp now = new Timestamp(System.currentTimeMillis());
            CartItem newItem = new CartItem(null, userId, productId, quantity, now.toLocalDateTime());
            try (Connection conn = DatabaseConnectionManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, userId);
                ps.setLong(2, productId);
                ps.setInt(3, quantity);
                ps.setTimestamp(4, now);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        newItem.setId(rs.getLong(1));
                    }
                }
            }
            return newItem;
        }
    }

    @Override
    public boolean updateQuantity(Long cartItemId, int quantity) throws SQLException {
        if (quantity <= 0) {
            String sql = "DELETE FROM cart_items WHERE id = ?";
            try (Connection conn = DatabaseConnectionManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, cartItemId);
                return ps.executeUpdate() > 0;
            }
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeItem(Long cartItemId, Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean clearCart(Connection conn, Long userId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                return ps.executeUpdate() >= 0;
            }
        } else {
            try (Connection localConn = DatabaseConnectionManager.getConnection();
                 PreparedStatement ps = localConn.prepareStatement(sql)) {
                ps.setLong(1, userId);
                return ps.executeUpdate() >= 0;
            }
        }
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        CartItem item = new CartItem(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
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
