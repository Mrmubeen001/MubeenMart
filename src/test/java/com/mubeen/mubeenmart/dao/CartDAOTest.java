package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.dao.impl.CartDAOImpl;
import com.mubeen.mubeenmart.dao.impl.ProductDAOImpl;
import com.mubeen.mubeenmart.dao.impl.UserDAOImpl;
import com.mubeen.mubeenmart.model.CartItem;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.model.Role;
import com.mubeen.mubeenmart.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CartDAO CRUD Tests")
class CartDAOTest extends BaseDAOTest {

    private CartDAO cartDAO;
    private Long userId;
    private Long productId;

    @BeforeEach
    void setUp() throws SQLException {
        cartDAO = new CartDAOImpl();
        UserDAO userDAO = new UserDAOImpl();
        ProductDAO productDAO = new ProductDAOImpl();

        String sellerEmail = "seller_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User seller = userDAO.create(new User(null, "Cart Seller", sellerEmail, "hash", Role.SELLER, LocalDateTime.now()));

        String buyerEmail = "buyer_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User buyer = userDAO.create(new User(null, "Cart Buyer", buyerEmail, "hash", Role.BUYER, LocalDateTime.now()));
        userId = buyer.getId();

        Product product = productDAO.create(new Product(
                null, seller.getId(), "Cart Test Item", "Desc",
                new BigDecimal("49.99"), 20, "Electronics", "img", LocalDateTime.now()
        ));
        productId = product.getId();
    }

    @Test
    @DisplayName("Should add, update, retrieve and clear cart items")
    void testCartOperations() throws SQLException {
        // 1. Add item
        CartItem added = cartDAO.addOrUpdateItem(userId, productId, 2);
        assertNotNull(added.getId());
        assertEquals(2, added.getQuantity());

        // 2. Increment quantity via duplicate add
        CartItem incremented = cartDAO.addOrUpdateItem(userId, productId, 1);
        assertEquals(3, incremented.getQuantity());

        // 3. Retrieve user cart
        List<CartItem> cart = cartDAO.findByUserId(userId);
        assertFalse(cart.isEmpty());
        assertEquals(1, cart.size());
        assertEquals("Cart Test Item", cart.get(0).getProduct().getName());

        // 4. Update exact quantity
        boolean updated = cartDAO.updateQuantity(cart.get(0).getId(), 5);
        assertTrue(updated);
        assertEquals(5, cartDAO.findByUserId(userId).get(0).getQuantity());

        // 5. Clear cart
        boolean cleared = cartDAO.clearCart(null, userId);
        assertTrue(cleared);
        assertTrue(cartDAO.findByUserId(userId).isEmpty());
    }
}
