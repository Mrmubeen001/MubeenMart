package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for order line items.
 */
public interface OrderItemDAO {

    /**
     * Finds all line items for a specific order.
     *
     * @param orderId the order ID
     * @return list of items
     * @throws SQLException on database error
     */
    List<OrderItem> findByOrderId(Long orderId) throws SQLException;

    /**
     * Inserts an order item using an existing transactional connection.
     *
     * @param conn database connection with transaction
     * @param item item to insert
     * @return created item with generated ID
     * @throws SQLException on database error
     */
    OrderItem create(Connection conn, OrderItem item) throws SQLException;

    /**
     * Finds items in an order that belong to a specific seller.
     *
     * @param orderId the order ID
     * @param sellerId the seller ID
     * @return items belonging to seller
     * @throws SQLException on database error
     */
    List<OrderItem> findByOrderIdAndSellerId(Long orderId, Long sellerId) throws SQLException;
}
