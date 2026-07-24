package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dto.OrderRequestDTO;
import com.mubeen.mubeenmart.dto.SalesSummaryDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.OrderStatus;
import com.mubeen.mubeenmart.model.Role;

import java.util.List;

/**
 * Service interface for checkout, order lifecycle management, and seller analytics.
 */
public interface OrderService {

    /**
     * Executes the checkout flow from current cart contents with mock payment confirmation.
     * Enforces transactional integrity across order creation, line items, stock deduction, and cart cleanup.
     *
     * @param buyerId buyer user ID
     * @param request checkout options (shipping address, mock payment method)
     * @return created Order
     * @throws AppException if validation fails, stock is insufficient, or payment fails
     */
    Order checkout(Long buyerId, OrderRequestDTO request) throws AppException;

    /**
     * Gets past orders placed by a buyer.
     *
     * @param buyerId buyer user ID
     * @return list of orders
     * @throws AppException on failure
     */
    List<Order> getBuyerOrders(Long buyerId) throws AppException;

    /**
     * Gets incoming orders containing products of a seller.
     *
     * @param sellerId seller user ID
     * @return list of orders
     * @throws AppException on failure
     */
    List<Order> getSellerOrders(Long sellerId) throws AppException;

    /**
     * Gets all orders in the system for administrative oversight.
     *
     * @return list of all orders
     * @throws AppException on failure
     */
    List<Order> getAllOrders() throws AppException;

    /**
     * Retrieves full order details with line items.
     *
     * @param orderId order ID
     * @param userId requesting user ID
     * @param role requesting user role
     * @return Order with line items populated
     * @throws AppException if unauthorized or not found
     */
    Order getOrderById(Long orderId, Long userId, Role role) throws AppException;

    /**
     * Updates an order status following the workflow: PENDING -> CONFIRMED -> SHIPPED -> DELIVERED.
     *
     * @param orderId order ID
     * @param newStatus new target status
     * @param requesterId requesting user ID
     * @param role requesting user role
     * @return true if updated
     * @throws AppException if unauthorized or order not found
     */
    boolean updateOrderStatus(Long orderId, OrderStatus newStatus, Long requesterId, Role role) throws AppException;

    /**
     * Generates a sales summary report for a seller's dashboard (O3 requirement).
     *
     * @param sellerId seller user ID
     * @return SalesSummaryDTO with order counts, units sold, revenue, and active listings
     * @throws AppException on failure
     */
    SalesSummaryDTO getSellerSalesSummary(Long sellerId) throws AppException;
}
