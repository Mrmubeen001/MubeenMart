package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for product catalog operations.
 */
public interface ProductDAO {

    /**
     * Finds a product by its ID.
     *
     * @param id product ID
     * @return Optional containing the product if found
     * @throws SQLException on database error
     */
    Optional<Product> findById(Long id) throws SQLException;

    /**
     * Retrieves all products in the catalog.
     *
     * @return list of all products
     * @throws SQLException on database error
     */
    List<Product> findAll() throws SQLException;

    /**
     * Searches and filters products by category and optional keyword query.
     *
     * @param category category filter (or null/all)
     * @param query keyword search in name/description (or null)
     * @return matching products
     * @throws SQLException on database error
     */
    List<Product> searchAndFilter(String category, String query) throws SQLException;

    /**
     * Finds all products listed by a specific seller.
     *
     * @param sellerId seller's user ID
     * @return list of seller's products
     * @throws SQLException on database error
     */
    List<Product> findBySellerId(Long sellerId) throws SQLException;

    /**
     * Inserts a new product listing.
     *
     * @param product product entity to insert
     * @return created product with generated ID
     * @throws SQLException on database error
     */
    Product create(Product product) throws SQLException;

    /**
     * Updates an existing product listing.
     *
     * @param product product entity to update
     * @return true if updated
     * @throws SQLException on database error
     */
    boolean update(Product product) throws SQLException;

    /**
     * Deletes a product by ID.
     *
     * @param id product ID
     * @return true if deleted
     * @throws SQLException on database error
     */
    boolean delete(Long id) throws SQLException;

    /**
     * Decrements the stock quantity of a product atomically.
     *
     * @param productId product ID
     * @param quantity quantity to deduct
     * @return true if successfully deducted, false if insufficient stock
     * @throws SQLException on database error
     */
    boolean deductStock(Long productId, int quantity) throws SQLException;

    /**
     * Retrieves distinct categories present in the catalog.
     *
     * @return list of category names
     * @throws SQLException on database error
     */
    List<String> findDistinctCategories() throws SQLException;
}
