package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.OrderStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for customer orders.
 */
public interface OrderDAO {

    /**
     * Finds an order by its ID.
     *
     * @param id order ID
     * @return Optional containing order
     * @throws SQLException on database error
     */
    Optional<Order> findById(Long id) throws SQLException;

    /**
     * Finds all orders placed by a specific buyer.
     *
     * @param buyerId buyer's user ID
     * @return list of orders
     * @throws SQLException on database error
     */
    List<Order> findByBuyerId(Long buyerId) throws SQLException;

    /**
     * Finds orders containing items belonging to a specific seller.
     *
     * @param sellerId seller's user ID
     * @return list of orders containing the seller's products
     * @throws SQLException on database error
     */
    List<Order> findBySellerId(Long sellerId) throws SQLException;

    /**
     * Retrieves all orders across the marketplace (for admin view).
     *
     * @return list of all orders
     * @throws SQLException on database error
     */
    List<Order> findAll() throws SQLException;

    /**
     * Creates a new order using the provided connection (for transaction support).
     *
     * @param conn database connection with active transaction
     * @param order order to create
     * @return created order with generated ID
     * @throws SQLException on database error
     */
    Order create(Connection conn, Order order) throws SQLException;

    /**
     * Updates the status of an order.
     *
     * @param orderId order ID
     * @param status new status
     * @return true if updated
     * @throws SQLException on database error
     */
    boolean updateStatus(Long orderId, OrderStatus status) throws SQLException;
}
