package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.dto.ProductRequestDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Product;

import java.util.List;

/**
 * Service interface for product listing management and catalog browsing.
 */
public interface ProductService {

    /**
     * Retrieves all active products in the marketplace catalog.
     *
     * @return list of products
     * @throws AppException on service failure
     */
    List<Product> getAllProducts() throws AppException;

    /**
     * Finds a single product by ID.
     *
     * @param id product ID
     * @return product entity
     * @throws AppException if product not found
     */
    Product getProductById(Long id) throws AppException;

    /**
     * Searches and filters products by category and search keyword.
     *
     * @param category category filter
     * @param query keyword search
     * @return list of matching products
     * @throws AppException on service failure
     */
    List<Product> searchProducts(String category, String query) throws AppException;

    /**
     * Finds all products listed by a seller.
     *
     * @param sellerId seller's user ID
     * @return list of products
     * @throws AppException on service failure
     */
    List<Product> getProductsBySeller(Long sellerId) throws AppException;

    /**
     * Creates a new product listing for a seller.
     *
     * @param sellerId seller's user ID
     * @param request product details DTO
     * @return created product
     * @throws AppException if validation fails
     */
    Product createProduct(Long sellerId, ProductRequestDTO request) throws AppException;

    /**
     * Updates an existing product listing.
     *
     * @param sellerId requester user ID
     * @param productId product ID
     * @param request updated details
     * @param isAdmin whether the requester is an administrator
     * @return updated product
     * @throws AppException if unauthorized or validation fails
     */
    Product updateProduct(Long sellerId, Long productId, ProductRequestDTO request, boolean isAdmin) throws AppException;

    /**
     * Deletes a product listing (moderation or seller removal).
     *
     * @param requesterId user ID requesting deletion
     * @param productId product ID
     * @param isAdmin whether requester is an administrator
     * @return true if deleted
     * @throws AppException if unauthorized or not found
     */
    boolean deleteProduct(Long requesterId, Long productId, boolean isAdmin) throws AppException;

    /**
     * Gets all distinct product categories.
     *
     * @return list of categories
     * @throws AppException on service failure
     */
    List<String> getCategories() throws AppException;
}
