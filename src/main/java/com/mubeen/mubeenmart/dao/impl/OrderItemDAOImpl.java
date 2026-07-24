package com.mubeen.mubeenmart.dao.impl;

import com.mubeen.mubeenmart.dao.OrderItemDAO;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link OrderItemDAO} using PreparedStatement and try-with-resources.
 */
public class OrderItemDAOImpl implements OrderItemDAO {

    private static final String BASE_SELECT =
            "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, oi.created_at, " +
            "p.name AS product_name, p.image_url AS product_image_url, p.seller_id " +
            "FROM order_items oi " +
            "JOIN products p ON oi.product_id = p.id ";

    @Override
    public List<OrderItem> findByOrderId(Long orderId) throws SQLException {
        String sql = BASE_SELECT + "WHERE oi.order_id = ? ORDER BY oi.id ASC";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return list;
    }

    @Override
    public OrderItem create(Connection conn, OrderItem item) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, created_at) VALUES (?, ?, ?, ?, ?)";
        Timestamp now = Timestamp.valueOf(item.getCreatedAt() != null ? item.getCreatedAt() : java.time.LocalDateTime.now());
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.setTimestamp(5, now);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getLong(1));
                }
            }
        }
        item.setCreatedAt(now.toLocalDateTime());
        return item;
    }

    @Override
    public List<OrderItem> findByOrderIdAndSellerId(Long orderId, Long sellerId) throws SQLException {
        String sql = BASE_SELECT + "WHERE oi.order_id = ? AND p.seller_id = ? ORDER BY oi.id ASC";
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return list;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        OrderItem item = new OrderItem(
                rs.getLong("id"),
                rs.getLong("order_id"),
                rs.getLong("product_id"),
                rs.getInt("quantity"),
                rs.getBigDecimal("unit_price"),
                ts != null ? ts.toLocalDateTime() : null
        );
        item.setProductName(rs.getString("product_name"));
        item.setProductImageUrl(rs.getString("product_image_url"));
        item.setSellerId(rs.getLong("seller_id"));
        return item;
    }
}
