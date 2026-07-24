package com.mubeen.mubeenmart.service.impl;

import com.mubeen.mubeenmart.dao.DAOFactory;
import com.mubeen.mubeenmart.dao.ProductDAO;
import com.mubeen.mubeenmart.dao.WishlistDAO;
import com.mubeen.mubeenmart.exception.*;
import com.mubeen.mubeenmart.model.WishlistItem;
import com.mubeen.mubeenmart.service.WishlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Service implementation for wishlist operations.
 */
public class WishlistServiceImpl implements WishlistService {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    private final WishlistDAO wishlistDAO;
    private final ProductDAO productDAO;

    public WishlistServiceImpl() {
        this(DAOFactory.getWishlistDAO(), DAOFactory.getProductDAO());
    }

    public WishlistServiceImpl(WishlistDAO wishlistDAO, ProductDAO productDAO) {
        this.wishlistDAO = wishlistDAO;
        this.productDAO = productDAO;
    }

    @Override
    public List<WishlistItem> getWishlist(Long userId) throws AppException {
        if (userId == null || userId <= 0) {
            throw new ValidationException("User must be logged in to view wishlist");
        }
        try {
            return wishlistDAO.findByUserId(userId);
        } catch (SQLException e) {
            logger.error("Database error loading wishlist for user: {}", userId, e);
            throw new AppException("Failed to load wishlist", 500, e);
        }
    }

    @Override
    public WishlistItem addToWishlist(Long userId, Long productId) throws AppException {
        if (userId == null || userId <= 0 || productId == null || productId <= 0) {
            throw new ValidationException("Invalid wishlist request parameters");
        }
        try {
            productDAO.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

            WishlistItem item = wishlistDAO.add(userId, productId);
            logger.info("Product added to wishlist: userId={}, productId={}", userId, productId);
            return item;
        } catch (SQLException e) {
            logger.error("Database error adding to wishlist: userId={}, productId={}", userId, productId, e);
            throw new AppException("Failed to add to wishlist", 500, e);
        }
    }

    @Override
    public boolean removeFromWishlist(Long userId, Long productId) throws AppException {
        if (userId == null || productId == null) {
            throw new ValidationException("Invalid wishlist removal parameters");
        }
        try {
            return wishlistDAO.remove(userId, productId);
        } catch (SQLException e) {
            logger.error("Database error removing from wishlist: userId={}, productId={}", userId, productId, e);
            throw new AppException("Failed to remove item from wishlist", 500, e);
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) throws AppException {
        if (userId == null || productId == null) return false;
        try {
            return wishlistDAO.isInWishlist(userId, productId);
        } catch (SQLException e) {
            logger.error("Database error checking wishlist status", e);
            return false;
        }
    }
}
