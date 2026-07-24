package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.dto.ProductRequestDTO;
import com.mubeen.mubeenmart.exception.AuthorizationException;
import com.mubeen.mubeenmart.exception.ValidationException;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Unit Tests (Mockito Mocked DAO)")
class ProductServiceTest {

    @Mock
    private ProductDAO productDAO;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productDAO);
    }

    @Test
    @DisplayName("Should create product listing with validation")
    void testCreateProductSuccess() throws Exception {
        ProductRequestDTO dto = new ProductRequestDTO(
                "Wireless Earbuds", "Deep bass stereo earbuds",
                new BigDecimal("59.99"), 15, "Electronics", "https://img.com/earbuds.jpg"
        );

        Product saved = new Product(1L, 2L, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStockQty(), dto.getCategory(), dto.getImageUrl(), LocalDateTime.now());
        when(productDAO.create(any(Product.class))).thenReturn(saved);

        Product result = productService.createProduct(2L, dto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Wireless Earbuds", result.getName());
    }

    @Test
    @DisplayName("Should reject invalid price or negative stock at service layer before DAO call")
    void testValidationAtTop() {
        ProductRequestDTO invalidPrice = new ProductRequestDTO("Item", "Desc", new BigDecimal("-10.00"), 5, "Electronics", null);
        assertThrows(ValidationException.class, () -> productService.createProduct(2L, invalidPrice));

        ProductRequestDTO negativeStock = new ProductRequestDTO("Item", "Desc", new BigDecimal("10.00"), -5, "Electronics", null);
        assertThrows(ValidationException.class, () -> productService.createProduct(2L, negativeStock));

        verifyNoInteractions(productDAO);
    }

    @Test
    @DisplayName("Should reject unauthorized update if requester is not seller or admin")
    void testUnauthorizedUpdate() throws Exception {
        Product existing = new Product(1L, 2L, "Product", "Desc", new BigDecimal("10.00"), 5, "Cat", null, LocalDateTime.now());
        when(productDAO.findById(1L)).thenReturn(Optional.of(existing));

        ProductRequestDTO updateDto = new ProductRequestDTO("Updated", "Desc", new BigDecimal("12.00"), 5, "Cat", null);

        // Requester 99L is neither seller (2L) nor admin
        assertThrows(AuthorizationException.class, () -> productService.updateProduct(99L, 1L, updateDto, false));
    }
}
