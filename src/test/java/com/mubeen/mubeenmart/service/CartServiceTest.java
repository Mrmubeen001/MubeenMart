package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dao.CartDAO;
import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.exception.ConflictException;
import com.mubeen.mubeenmart.model.CartItem;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CartService Unit Tests (Mockito Mocked DAOs)")
class CartServiceTest {

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl(cartDAO, productDAO);
    }

    @Test
    @DisplayName("Should add product to cart when stock is sufficient")
    void testAddToCartSuccess() throws Exception {
        Product product = new Product(1L, 2L, "Mouse", "Desc", new BigDecimal("25.00"), 10, "Electronics", null, LocalDateTime.now());
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        CartItem item = new CartItem(100L, 5L, 1L, 2, LocalDateTime.now());
        when(cartDAO.addOrUpdateItem(5L, 1L, 2)).thenReturn(item);

        CartItem result = cartService.addToCart(5L, 1L, 2);
        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        assertEquals("Mouse", result.getProduct().getName());
    }

    @Test
    @DisplayName("Should reject adding to cart when quantity exceeds available stock")
    void testAddToCartInsufficientStock() throws Exception {
        Product product = new Product(1L, 2L, "Mouse", "Desc", new BigDecimal("25.00"), 3, "Electronics", null, LocalDateTime.now());
        when(productDAO.findById(1L)).thenReturn(Optional.of(product));

        // Attempt to request 10 units when only 3 are in stock
        assertThrows(ConflictException.class, () -> cartService.addToCart(5L, 1L, 10));
        verify(cartDAO, never()).addOrUpdateItem(anyLong(), anyLong(), anyInt());
    }

    @Test
    @DisplayName("Should calculate running total correctly")
    void testRunningTotal() {
        Product p1 = new Product(1L, 2L, "A", "D", new BigDecimal("10.00"), 5, "C", null, LocalDateTime.now());
        Product p2 = new Product(2L, 2L, "B", "D", new BigDecimal("25.50"), 5, "C", null, LocalDateTime.now());

        CartItem i1 = new CartItem(1L, 5L, 1L, 2, LocalDateTime.now());
        i1.setProduct(p1); // 2 * 10 = 20.00
        CartItem i2 = new CartItem(2L, 5L, 2L, 1, LocalDateTime.now());
        i2.setProduct(p2); // 1 * 25.50 = 25.50

        BigDecimal total = cartService.calculateRunningTotal(List.of(i1, i2));
        assertEquals(new BigDecimal("45.50"), total);
    }
}
