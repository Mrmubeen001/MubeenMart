package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.dao.*;
import com.mubeen.mubeenmart.dto.OrderRequestDTO;
import com.mubeen.mubeenmart.dto.SalesSummaryDTO;
import com.mubeen.mubeenmart.exception.*;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.*;
import com.mubeen.mubeenmart.service.OrderService;
import com.mubeen.mubeenmart.service.PaymentResult;
import com.mubeen.mubeenmart.service.PaymentStrategy;
import com.mubeen.mubeenmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation managing order checkout transaction, order tracking, and seller sales metrics.
 */
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderDAO orderDAO;
    private final OrderItemDAO orderItemDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl() {
        this(DAOFactory.getOrderDAO(), DAOFactory.getOrderItemDAO(), DAOFactory.getCartDAO(), DAOFactory.getProductDAO());
    }

    public OrderServiceImpl(OrderDAO orderDAO, OrderItemDAO orderItemDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.orderItemDAO = orderItemDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Order checkout(Long buyerId, OrderRequestDTO request) throws AppException {
        // 1. Top of method validation
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("Invalid buyer ID");
        }
        if (request == null) {
            throw new ValidationException("Order request payload is required");
        }
        ValidationUtil.requireNonBlank(request.getShippingAddress(), "Shipping address");
        ValidationUtil.requireNonBlank(request.getPaymentMethod(), "Payment method");

        try {
            // Retrieve cart contents
            List<CartItem> cartItems = cartDAO.findByUserId(buyerId);
            if (cartItems.isEmpty()) {
                throw new ValidationException("Your shopping cart is empty. Please add items before checking out.");
            }

            // Verify availability and compute total
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (CartItem ci : cartItems) {
                Product product = productDAO.findById(ci.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product '" + ci.getProduct().getName() + "' no longer exists"));
                if (product.getStockQty() < ci.getQuantity()) {
                    throw new ConflictException("Insufficient stock for '" + product.getName() + "'. Available: " + product.getStockQty());
                }
                BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
                totalAmount = totalAmount.add(lineTotal);
            }

            // Execute Mock Payment Confirmation via Strategy pattern
            PaymentStrategy paymentStrategy = PaymentStrategyFactory.getStrategy(request.getPaymentMethod());
            PaymentResult paymentResult = paymentStrategy.processPayment(buyerId, totalAmount, request.getShippingAddress());
            if (!paymentResult.isSuccessful()) {
                throw new AppException("Mock payment confirmation failed: " + paymentResult.getMessage(), 400);
            }

            logger.info("Payment confirmed [{}] for buyer ID {}: amount={}", paymentResult.getTransactionId(), buyerId, totalAmount);

            // Execute transactional order creation, stock deduction, and cart cleanup
            Order createdOrder;
            try (Connection conn = DatabaseConnectionManager.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    Order order = new Order(null, buyerId, OrderStatus.CONFIRMED, totalAmount, LocalDateTime.now());
                    createdOrder = orderDAO.create(conn, order);

                    for (CartItem ci : cartItems) {
                        OrderItem item = new OrderItem(
                                null,
                                createdOrder.getId(),
                                ci.getProductId(),
                                ci.getQuantity(),
                                ci.getProduct().getPrice(),
                                LocalDateTime.now()
                        );
                        orderItemDAO.create(conn, item);

                        // Atomically deduct product stock
                        boolean deducted = productDAO.deductStock(ci.getProductId(), ci.getQuantity());
                        if (!deducted) {
                            throw new ConflictException("Stock deduction failed for product ID " + ci.getProductId());
                        }
                    }

                    // Clear cart
                    cartDAO.clearCart(conn, buyerId);

                    conn.commit();
                    logger.info("Order placed successfully: orderId={}, buyerId={}, total={}", createdOrder.getId(), buyerId, totalAmount);
                } catch (Exception ex) {
                    conn.rollback();
                    logger.error("Transaction rolled back during order placement", ex);
                    if (ex instanceof AppException) {
                        throw (AppException) ex;
                    }
                    throw new AppException("Failed to complete order checkout", 500, ex);
                } finally {
                    conn.setAutoCommit(true);
                }
            }

            // Populate items for return
            createdOrder.setItems(orderItemDAO.findByOrderId(createdOrder.getId()));
            return createdOrder;

        } catch (SQLException e) {
            logger.error("Database error during checkout for buyer: {}", buyerId, e);
            throw new AppException("Failed to place order due to database error", 500, e);
        }
    }

    @Override
    public List<Order> getBuyerOrders(Long buyerId) throws AppException {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("Invalid buyer ID");
        }
        try {
            List<Order> orders = orderDAO.findByBuyerId(buyerId);
            for (Order o : orders) {
                o.setItems(orderItemDAO.findByOrderId(o.getId()));
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Database error getting buyer orders: {}", buyerId, e);
            throw new AppException("Failed to load buyer orders", 500, e);
        }
    }

    @Override
    public List<Order> getSellerOrders(Long sellerId) throws AppException {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Invalid seller ID");
        }
        try {
            List<Order> orders = orderDAO.findBySellerId(sellerId);
            for (Order o : orders) {
                o.setItems(orderItemDAO.findByOrderIdAndSellerId(o.getId(), sellerId));
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Database error getting seller orders: {}", sellerId, e);
            throw new AppException("Failed to load seller orders", 500, e);
        }
    }

    @Override
    public List<Order> getAllOrders() throws AppException {
        try {
            List<Order> orders = orderDAO.findAll();
            for (Order o : orders) {
                o.setItems(orderItemDAO.findByOrderId(o.getId()));
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Database error getting all orders", e);
            throw new AppException("Failed to load marketplace orders", 500, e);
        }
    }

    @Override
    public Order getOrderById(Long orderId, Long userId, Role role) throws AppException {
        if (orderId == null || orderId <= 0) {
            throw new ValidationException("Invalid order ID");
        }
        try {
            Order order = orderDAO.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

            // Authorization check
            if (role != Role.ADMIN && !order.getBuyerId().equals(userId)) {
                // If seller, check if seller has items in order
                List<OrderItem> sellerItems = orderItemDAO.findByOrderIdAndSellerId(orderId, userId);
                if (sellerItems.isEmpty()) {
                    throw new AuthorizationException("You are not authorized to view this order");
                }
                order.setItems(sellerItems);
                return order;
            }

            order.setItems(orderItemDAO.findByOrderId(orderId));
            return order;
        } catch (SQLException e) {
            logger.error("Database error loading order ID: {}", orderId, e);
            throw new AppException("Failed to load order details", 500, e);
        }
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus newStatus, Long requesterId, Role role) throws AppException {
        if (orderId == null || newStatus == null) {
            throw new ValidationException("Order ID and new status are required");
        }
        try {
            Order order = orderDAO.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

            if (role != Role.ADMIN) {
                // Only seller who owns items in this order can update status
                List<OrderItem> sellerItems = orderItemDAO.findByOrderIdAndSellerId(orderId, requesterId);
                if (sellerItems.isEmpty()) {
                    throw new AuthorizationException("Only the seller or admin can update order status");
                }
            }

            boolean updated = orderDAO.updateStatus(orderId, newStatus);
            logger.info("Order status updated: orderId={}, newStatus={}, updatedBy={}", orderId, newStatus, requesterId);
            return updated;
        } catch (SQLException e) {
            logger.error("Database error updating status for order: {}", orderId, e);
            throw new AppException("Failed to update order status", 500, e);
        }
    }

    @Override
    public SalesSummaryDTO getSellerSalesSummary(Long sellerId) throws AppException {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Invalid seller ID");
        }
        try {
            List<Order> sellerOrders = orderDAO.findBySellerId(sellerId);
            int totalOrders = sellerOrders.size();
            int totalUnitsSold = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;

            for (Order o : sellerOrders) {
                List<OrderItem> items = orderItemDAO.findByOrderIdAndSellerId(o.getId(), sellerId);
                for (OrderItem item : items) {
                    totalUnitsSold += item.getQuantity();
                    totalRevenue = totalRevenue.add(item.getSubtotal());
                }
            }

            List<Product> products = productDAO.findBySellerId(sellerId);
            int activeListings = products.size();

            return new SalesSummaryDTO(totalOrders, totalUnitsSold, totalRevenue, activeListings);
        } catch (SQLException e) {
            logger.error("Database error computing seller summary for seller: {}", sellerId, e);
            throw new AppException("Failed to calculate seller sales summary", 500, e);
        }
    }
}
