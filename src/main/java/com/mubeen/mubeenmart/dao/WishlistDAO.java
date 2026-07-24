package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.WishlistItem;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for wishlist management (O1 requirement).
 */
public interface WishlistDAO {

    /**
     * Finds all wishlist items for a given user with product details populated.
     *
     * @param userId user ID
     * @return list of wishlist items
     * @throws SQLException on database error
     */
    List<WishlistItem> findByUserId(Long userId) throws SQLException;

    /**
     * Adds an item to the user's wishlist if not already present.
     *
     * @param userId user ID
     * @param productId product ID
     * @return created WishlistItem
     * @throws SQLException on database error
     */
    WishlistItem add(Long userId, Long productId) throws SQLException;

    /**
     * Removes an item from the user's wishlist.
     *
     * @param userId user ID
     * @param productId product ID
     * @return true if removed
     * @throws SQLException on database error
     */
    boolean remove(Long userId, Long productId) throws SQLException;

    /**
     * Checks if a product is in a user's wishlist.
     *
     * @param userId user ID
     * @param productId product ID
     * @return true if in wishlist
     * @throws SQLException on database error
     */
    boolean isInWishlist(Long userId, Long productId) throws SQLException;
}
