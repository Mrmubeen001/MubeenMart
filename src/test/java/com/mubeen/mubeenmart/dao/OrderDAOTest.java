package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.dao.impl.OrderDAOImpl;
import com.mubeen.mubeenmart.dao.impl.OrderItemDAOImpl;
import com.mubeen.mubeenmart.dao.impl.ProductDAOImpl;
import com.mubeen.mubeenmart.dao.impl.UserDAOImpl;
import com.mubeen.mubeenmart.listener.DatabaseConnectionManager;
import com.mubeen.mubeenmart.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderDAO CRUD Tests")
class OrderDAOTest extends BaseDAOTest {

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private Long buyerId;
    private Long sellerId;
    private Long productId;

    @BeforeEach
    void setUp() throws SQLException {
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();

        String sellerEmail = "seller_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User seller = userDAO.create(new User(null, "Order Seller", sellerEmail, "hash", Role.SELLER, LocalDateTime.now()));
        sellerId = seller.getId();

        String buyerEmail = "buyer_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User buyer = userDAO.create(new User(null, "Order Buyer", buyerEmail, "hash", Role.BUYER, LocalDateTime.now()));
        buyerId = buyer.getId();

        Product product = productDAO.create(new Product(
                null, sellerId, "Order Test Product", "Desc",
                new BigDecimal("75.00"), 10, "Fashion", "img", LocalDateTime.now()
        ));
        productId = product.getId();
    }

    @Test
    @DisplayName("Should create order, add line items, update status, and query by buyer/seller")
    void testOrderLifecycle() throws SQLException {
        Order order = new Order(null, buyerId, OrderStatus.CONFIRMED, new BigDecimal("150.00"), LocalDateTime.now());

        Order created;
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            created = orderDAO.create(conn, order);
            assertNotNull(created.getId());

            OrderItem item = new OrderItem(null, created.getId(), productId, 2, new BigDecimal("75.00"), LocalDateTime.now());
            orderItemDAO.create(conn, item);
        }

        // 1. Find by ID
        Optional<Order> found = orderDAO.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals(OrderStatus.CONFIRMED, found.get().getStatus());
        assertEquals(new BigDecimal("150.00"), found.get().getTotalAmount());

        // 2. Query line items
        List<OrderItem> items = orderItemDAO.findByOrderId(created.getId());
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());

        // 3. Query by Buyer
        List<Order> buyerOrders = orderDAO.findByBuyerId(buyerId);
        assertFalse(buyerOrders.isEmpty());

        // 4. Query by Seller
        List<Order> sellerOrders = orderDAO.findBySellerId(sellerId);
        assertFalse(sellerOrders.isEmpty());

        // 5. Update Status
        boolean updated = orderDAO.updateStatus(created.getId(), OrderStatus.DELIVERED);
        assertTrue(updated);
        assertEquals(OrderStatus.DELIVERED, orderDAO.findById(created.getId()).get().getStatus());
    }
}
