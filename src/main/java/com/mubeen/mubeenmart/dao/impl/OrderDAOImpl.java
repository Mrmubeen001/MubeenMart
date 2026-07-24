package com.mubeen.mubeenmart.dao.impl;

import com.mubeen.mubeenmart.dao.OrderDAO;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of {@link OrderDAO} using PreparedStatement and try-with-resources.
 */
public class OrderDAOImpl implements OrderDAO {

    private static final String BASE_SELECT =
            "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, u.name AS buyer_name, u.email AS buyer_email " +
            "FROM orders o JOIN users u ON o.buyer_id = u.id ";

    @Override
    public Optional<Order> findById(Long id) throws SQLException {
        String sql = BASE_SELECT + "WHERE o.id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrder(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) throws SQLException {
        String sql = BASE_SELECT + "WHERE o.buyer_id = ? ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) throws SQLException {
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, u.name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE p.seller_id = ? " +
                     "ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToOrder(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Order> findAll() throws SQLException {
        String sql = BASE_SELECT + "ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToOrder(rs));
            }
        }
        return list;
    }

    @Override
    public Order create(Connection conn, Order order) throws SQLException {
        String sql = "INSERT INTO orders (buyer_id, status, total_amount, created_at) VALUES (?, ?, ?, ?)";
        Timestamp now = Timestamp.valueOf(order.getCreatedAt() != null ? order.getCreatedAt() : java.time.LocalDateTime.now());
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, order.getBuyerId());
            ps.setString(2, order.getStatus().name());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setTimestamp(4, now);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    order.setId(rs.getLong(1));
                }
            }
        }
        order.setCreatedAt(now.toLocalDateTime());
        return order;
    }

    @Override
    public boolean updateStatus(Long orderId, OrderStatus status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        Order order = new Order(
                rs.getLong("id"),
                rs.getLong("buyer_id"),
                OrderStatus.fromString(rs.getString("status")),
                rs.getBigDecimal("total_amount"),
                ts != null ? ts.toLocalDateTime() : null
        );
        order.setBuyerName(rs.getString("buyer_name"));
        order.setBuyerEmail(rs.getString("buyer_email"));
        return order;
    }
}
