package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.dao.CartDAO;
import com.mubeen.mubeenmart.dao.DAOFactory;
import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.exception.*;
import com.mubeen.mubeenmart.model.CartItem;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Service implementation for cart business operations.
 */
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl() {
        this(DAOFactory.getCartDAO(), DAOFactory.getProductDAO());
    }

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<CartItem> getCart(Long userId) throws AppException {
        if (userId == null || userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
        try {
            return cartDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Database error retrieving cart for user: {}", userId, e);
            throw new AppException("Failed to load shopping cart", 500, e);
        }
    }

    @Override
    public BigDecimal calculateRunningTotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getItemTotal());
        }
        return total;
    }

    @Override
    public CartItem addToCart(Long userId, Long productId, int quantity) throws AppException {
        // Top-level validation
        if (userId == null || userId <= 0) {
            throw new ValidationException("User must be logged in to add items to cart");
        }
        if (productId == null || productId <= 0) {
            throw new ValidationException("Invalid product ID");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be greater than zero");
        }

        try {
            Product product = productDAO.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

            if (product.getStockQty() < quantity) {
                throw new ConflictException("Insufficient stock available for " + product.getName() +
                        ". In stock: " + product.getStockQty());
            }

            CartItem cartItem = cartDAO.addOrUpdateItem(userId, productId, quantity);
            cartItem.setProduct(product);
            logger.info("Added product to cart: userId={}, productId={}, quantity={}", userId, productId, quantity);
            return cartItem;
        } catch (SQLException e) {
            logger.error("Database error adding to cart: userId={}, productId={}", userId, productId, e);
            throw new AppException("Failed to add item to cart", 500, e);
        }
    }

    @Override
    public CartItem updateQuantity(Long userId, Long cartItemId, int quantity) throws AppException {
        if (userId == null || userId <= 0 || cartItemId == null || cartItemId <= 0) {
            throw new ValidationException("Invalid cart update request");
        }
        if (quantity <= 0) {
            removeFromCart(userId, cartItemId);
            return null;
        }

        try {
            List<CartItem> items = cartDAO.findByUserId(userId);
            CartItem target = items.stream()
                    .filter(i -> i.getId().equals(cartItemId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

            Product product = productDAO.findById(target.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product no longer available"));

            if (product.getStockQty() < quantity) {
                throw new ConflictException("Cannot set quantity to " + quantity + ". Available stock: " + product.getStockQty());
            }

            cartDAO.updateQuantity(cartItemId, quantity);
            target.setQuantity(quantity);
            target.setProduct(product);
            return target;
        } catch (SQLException e) {
            logger.error("Database error updating cart item quantity: cartItemId={}", cartItemId, e);
            throw new AppException("Failed to update cart quantity", 500, e);
        }
    }

    @Override
    public boolean removeFromCart(Long userId, Long cartItemId) throws AppException {
        if (userId == null || cartItemId == null) {
            throw new ValidationException("Invalid cart item removal request");
        }
        try {
            return cartDAO.removeItem(cartItemId, userId);
        } catch (SQLException e) {
            logger.error("Database error removing cart item: cartItemId={}", cartItemId, e);
            throw new AppException("Failed to remove item from cart", 500, e);
        }
    }

    @Override
    public void clearCart(Long userId) throws AppException {
        if (userId == null) {
            return;
        }
        try {
            cartDAO.clearCart(null, userId);
        } catch (SQLException e) {
            logger.error("Database error clearing cart for user: {}", userId, e);
            throw new AppException("Failed to clear shopping cart", 500, e);
        }
    }
}
