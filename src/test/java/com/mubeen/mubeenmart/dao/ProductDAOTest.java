package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.dao.impl.ProductDAOImpl;
import com.mubeen.mubeenmart.dao.impl.UserDAOImpl;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ProductDAO CRUD Tests")
class ProductDAOTest extends BaseDAOTest {

    private ProductDAO productDAO;
    private UserDAO userDAO;
    private Long sellerId;

    @BeforeEach
    void setUp() throws SQLException {
        productDAO = new ProductDAOImpl();
        userDAO = new UserDAOImpl();

        // Create a test seller
        String email = "seller_" + UUID.randomUUID().toString().substring(0, 6) + "@test.com";
        User seller = userDAO.create(new User(null, "Test Seller", email, "hash", Role.SELLER, LocalDateTime.now()));
        sellerId = seller.getId();
    }

    @Test
    @DisplayName("Should create product, search, filter, deduct stock and delete")
    void testProductLifecycle() throws SQLException {
        Product p = new Product(
                null,
                sellerId,
                "Noise Cancelling Headphones",
                "High quality wireless ANC headphones",
                new BigDecimal("129.99"),
                20,
                "Electronics",
                "https://example.com/headphones.jpg",
                LocalDateTime.now()
        );

        // 1. Create
        Product created = productDAO.create(p);
        assertNotNull(created.getId());

        // 2. Find by ID
        Optional<Product> found = productDAO.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("Noise Cancelling Headphones", found.get().getName());
        assertEquals(new BigDecimal("129.99"), found.get().getPrice());

        // 3. Search and filter
        List<Product> searchResults = productDAO.searchAndFilter("Electronics", "wireless");
        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.stream().anyMatch(item -> item.getId().equals(created.getId())));

        // 4. Atomic stock deduction
        boolean deducted = productDAO.deductStock(created.getId(), 5);
        assertTrue(deducted);
        Product afterDeduct = productDAO.findById(created.getId()).get();
        assertEquals(15, afterDeduct.getStockQty());

        // Stock deduction failure on insufficient stock
        boolean failDeduct = productDAO.deductStock(created.getId(), 50);
        assertFalse(failDeduct);

        // 5. Delete
        boolean deleted = productDAO.delete(created.getId());
        assertTrue(deleted);
        assertFalse(productDAO.findById(created.getId()).isPresent());
    }
}
