package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.WishlistItem;

import java.util.List;

/**
 * Service interface for customer wishlist / save-for-later management (O1 requirement).
 */
public interface WishlistService {

    /**
     * Gets all wishlist items for a user.
     *
     * @param userId user ID
     * @return list of wishlist items
     * @throws AppException on failure
     */
    List<WishlistItem> getWishlist(Long userId) throws AppException;

    /**
     * Adds a product to the user's wishlist.
     *
     * @param userId user ID
     * @param productId product ID
     * @return created or existing WishlistItem
     * @throws AppException on failure
     */
    WishlistItem addToWishlist(Long userId, Long productId) throws AppException;

    /**
     * Removes an item from the user's wishlist.
     *
     * @param userId user ID
     * @param productId product ID
     * @return true if removed
     * @throws AppException on failure
     */
    boolean removeFromWishlist(Long userId, Long productId) throws AppException;

    /**
     * Checks if a product is in a user's wishlist.
     *
     * @param userId user ID
     * @param productId product ID
     * @return true if present
     * @throws AppException on failure
     */
    boolean isInWishlist(Long userId, Long productId) throws AppException;
}
