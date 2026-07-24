package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for shopping cart operations.
 */
public interface CartService {

    /**
     * Gets all items in a buyer's cart with up-to-date product information.
     *
     * @param userId buyer user ID
     * @return list of cart items
     * @throws AppException on failure
     */
    List<CartItem> getCart(Long userId) throws AppException;

    /**
     * Computes the running subtotal of all items in the cart.
     *
     * @param items list of cart items
     * @return running total amount
     */
    BigDecimal calculateRunningTotal(List<CartItem> items);

    /**
     * Adds a product to the cart with the specified quantity.
     *
     * @param userId buyer user ID
     * @param productId product ID
     * @param quantity quantity to add
     * @return created or updated CartItem
     * @throws AppException if product not found or insufficient stock
     */
    CartItem addToCart(Long userId, Long productId, int quantity) throws AppException;

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param userId buyer user ID
     * @param cartItemId cart item ID
     * @param quantity new quantity (if 0, item is removed)
     * @return updated CartItem or null if removed
     * @throws AppException if validation fails or insufficient stock
     */
    CartItem updateQuantity(Long userId, Long cartItemId, int quantity) throws AppException;

    /**
     * Removes an item from the cart.
     *
     * @param userId buyer user ID
     * @param cartItemId cart item ID
     * @return true if removed
     * @throws AppException on failure
     */
    boolean removeFromCart(Long userId, Long cartItemId) throws AppException;

    /**
     * Clears all items in the cart.
     *
     * @param userId buyer user ID
     * @throws AppException on failure
     */
    void clearCart(Long userId) throws AppException;
}
