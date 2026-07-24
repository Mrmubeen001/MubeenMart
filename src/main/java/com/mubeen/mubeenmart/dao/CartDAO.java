package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.CartItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for user cart persistence.
 */
public interface CartDAO {

    /**
     * Retrieves all items in a user's shopping cart with product details populated.
     *
     * @param userId user ID
     * @return list of cart items
     * @throws SQLException on database error
     */
    List<CartItem> findByUserId(Long userId) throws SQLException;

    /**
     * Finds a specific cart item for a user and product.
     *
     * @param userId user ID
     * @param productId product ID
     * @return Optional containing CartItem if present
     * @throws SQLException on database error
     */
    Optional<CartItem> findByUserAndProduct(Long userId, Long productId) throws SQLException;

    /**
     * Adds an item to the cart or increments its quantity.
     *
     * @param userId user ID
     * @param productId product ID
     * @param quantity quantity to add
     * @return updated or created CartItem
     * @throws SQLException on database error
     */
    CartItem addOrUpdateItem(Long userId, Long productId, int quantity) throws SQLException;

    /**
     * Updates the exact quantity of a cart item.
     *
     * @param cartItemId cart item ID
     * @param quantity new quantity
     * @return true if updated
     * @throws SQLException on database error
     */
    boolean updateQuantity(Long cartItemId, int quantity) throws SQLException;

    /**
     * Removes an item from the cart.
     *
     * @param cartItemId cart item ID
     * @param userId user ID (for security verification)
     * @return true if removed
     * @throws SQLException on database error
     */
    boolean removeItem(Long cartItemId, Long userId) throws SQLException;

    /**
     * Clears all items in a user's cart (typically after checkout).
     *
     * @param conn optional transactional connection (may be null)
     * @param userId user ID
     * @return true if cleared
     * @throws SQLException on database error
     */
    boolean clearCart(Connection conn, Long userId) throws SQLException;
}
