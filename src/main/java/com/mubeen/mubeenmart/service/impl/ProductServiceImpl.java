package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.dao.DAOFactory;
import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.dto.ProductRequestDTO;
import com.mubeen.mubeenmart.exception.*;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.service.ProductService;
import com.mubeen.mubeenmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Business service implementation for catalog products.
 */
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductDAO productDAO;

    public ProductServiceImpl() {
        this(DAOFactory.getProductDAO());
    }

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> getAllProducts() throws AppException {
        try {
            return productDAO.findAll();
        } catch (SQLException e) {
            logger.error("Database error retrieving all products", e);
            throw new AppException("Failed to load products", 500, e);
        }
    }

    @Override
    public Product getProductById(Long id) throws AppException {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid product ID");
        }
        try {
            return productDAO.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
        } catch (SQLException e) {
            logger.error("Database error retrieving product by ID: {}", id, e);
            throw new AppException("Failed to load product", 500, e);
        }
    }

    @Override
    public List<Product> searchProducts(String category, String query) throws AppException {
        try {
            return productDAO.searchAndFilter(category, query);
        } catch (SQLException e) {
            logger.error("Database error filtering products", e);
            throw new AppException("Failed to search products", 500, e);
        }
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) throws AppException {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("Invalid seller ID");
        }
        try {
            return productDAO.findBySellerId(sellerId);
        } catch (SQLException e) {
            logger.error("Database error retrieving seller products for seller: {}", sellerId, e);
            throw new AppException("Failed to load seller listings", 500, e);
        }
    }

    @Override
    public Product createProduct(Long sellerId, ProductRequestDTO request) throws AppException {
        // Validation at top of service method
        validateProductRequest(request);

        Product product = new Product(
                null,
                sellerId,
                request.getName().trim(),
                request.getDescription() != null ? request.getDescription().trim() : "",
                request.getPrice(),
                request.getStockQty(),
                request.getCategory().trim(),
                request.getImageUrl() != null ? request.getImageUrl().trim() : "",
                LocalDateTime.now()
        );

        try {
            Product created = productDAO.create(product);
            logger.info("Product created: id={}, sellerId={}, name='{}'", created.getId(), sellerId, created.getName());
            return created;
        } catch (SQLException e) {
            logger.error("Database error creating product", e);
            throw new AppException("Failed to save product", 500, e);
        }
    }

    @Override
    public Product updateProduct(Long sellerId, Long productId, ProductRequestDTO request, boolean isAdmin) throws AppException {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Invalid product ID");
        }
        validateProductRequest(request);

        Product existing = getProductById(productId);
        if (!isAdmin && !existing.getSellerId().equals(sellerId)) {
            throw new AuthorizationException("You are not authorized to update this listing");
        }

        existing.setName(request.getName().trim());
        existing.setDescription(request.getDescription() != null ? request.getDescription().trim() : "");
        existing.setPrice(request.getPrice());
        existing.setStockQty(request.getStockQty());
        existing.setCategory(request.getCategory().trim());
        if (request.getImageUrl() != null && !request.getImageUrl().trim().isEmpty()) {
            existing.setImageUrl(request.getImageUrl().trim());
        }

        try {
            boolean updated = productDAO.update(existing);
            if (!updated) {
                throw new AppException("Failed to update product", 500);
            }
            logger.info("Product updated: id={}, sellerId={}", productId, sellerId);
            return existing;
        } catch (SQLException e) {
            logger.error("Database error updating product id: {}", productId, e);
            throw new AppException("Failed to update product", 500, e);
        }
    }

    @Override
    public boolean deleteProduct(Long requesterId, Long productId, boolean isAdmin) throws AppException {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Invalid product ID");
        }
        Product existing = getProductById(productId);
        if (!isAdmin && !existing.getSellerId().equals(requesterId)) {
            throw new AuthorizationException("You are not authorized to delete this listing");
        }

        try {
            boolean deleted = productDAO.delete(productId);
            logger.info("Product deleted: id={}, requesterId={}, isAdmin={}", productId, requesterId, isAdmin);
            return deleted;
        } catch (SQLException e) {
            logger.error("Database error deleting product id: {}", productId, e);
            throw new AppException("Failed to delete product", 500, e);
        }
    }

    @Override
    public List<String> getCategories() throws AppException {
        try {
            return productDAO.findDistinctCategories();
        } catch (SQLException e) {
            logger.error("Database error fetching categories", e);
            throw new AppException("Failed to fetch product categories", 500, e);
        }
    }

    private void validateProductRequest(ProductRequestDTO request) throws ValidationException {
        if (request == null) {
            throw new ValidationException("Product payload is required");
        }
        ValidationUtil.requireNonBlank(request.getName(), "Product name");
        ValidationUtil.requireNonBlank(request.getCategory(), "Category");
        ValidationUtil.requirePositive(request.getPrice(), "Price");
        if (request.getStockQty() == null) {
            throw new ValidationException("Stock quantity is required");
        }
        ValidationUtil.requireNonNegative(request.getStockQty(), "Stock quantity");
    }
}
