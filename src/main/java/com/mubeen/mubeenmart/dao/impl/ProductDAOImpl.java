package com.mubeen.mubeenmart.dao.impl;

import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of {@link ProductDAO} using PreparedStatement and try-with-resources.
 */
public class ProductDAOImpl implements ProductDAO {

    private static final String BASE_SELECT =
            "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, " +
            "u.name AS seller_name, " +
            "COALESCE(AVG(r.rating), 0.0) AS avg_rating, " +
            "COUNT(r.id) AS review_count " +
            "FROM products p " +
            "JOIN users u ON p.seller_id = u.id " +
            "LEFT JOIN reviews r ON p.id = r.product_id ";

    private static final String GROUP_BY =
            " GROUP BY p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, p.category, p.image_url, p.created_at, u.name ";

    @Override
    public Optional<Product> findById(Long id) throws SQLException {
        String sql = BASE_SELECT + " WHERE p.id = ? " + GROUP_BY;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String sql = BASE_SELECT + GROUP_BY + " ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        }
        return list;
    }

    @Override
    public List<Product> searchAndFilter(String category, String query) throws SQLException {
        StringBuilder sql = new StringBuilder(BASE_SELECT);
        List<Object> params = new ArrayList<>();
        boolean hasCategory = category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category.trim());
        boolean hasQuery = query != null && !query.trim().isEmpty();

        if (hasCategory && hasQuery) {
            sql.append(" WHERE LOWER(p.category) = LOWER(?) AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            params.add(category.trim());
            String pattern = "%" + query.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        } else if (hasCategory) {
            sql.append(" WHERE LOWER(p.category) = LOWER(?) ");
            params.add(category.trim());
        } else if (hasQuery) {
            sql.append(" WHERE (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
            String pattern = "%" + query.trim().toLowerCase() + "%";
            params.add(pattern);
            params.add(pattern);
        }

        sql.append(GROUP_BY).append(" ORDER BY p.id DESC");

        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) throws SQLException {
        String sql = BASE_SELECT + " WHERE p.seller_id = ? " + GROUP_BY + " ORDER BY p.id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Product create(Product product) throws SQLException {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Timestamp now = Timestamp.valueOf(product.getCreatedAt() != null ? product.getCreatedAt() : java.time.LocalDateTime.now());
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getImageUrl());
            ps.setTimestamp(8, now);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getLong(1));
                }
            }
        }
        product.setCreatedAt(now.toLocalDateTime());
        return product;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_qty = ?, category = ?, image_url = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStockQty());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            ps.setLong(7, product.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deductStock(Long productId, int quantity) throws SQLException {
        String sql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, productId);
            ps.setInt(3, quantity);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> findDistinctCategories() throws SQLException {
        String sql = "SELECT DISTINCT category FROM products ORDER BY category ASC";
        List<String> categories = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        }
        return categories;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        Product p = new Product(
                rs.getLong("id"),
                rs.getLong("seller_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBigDecimal("price"),
                rs.getInt("stock_qty"),
                rs.getString("category"),
                rs.getString("image_url"),
                ts != null ? ts.toLocalDateTime() : null
        );
        p.setSellerName(rs.getString("seller_name"));
        p.setAverageRating(rs.getDouble("avg_rating"));
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
