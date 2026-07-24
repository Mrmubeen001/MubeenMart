package com.mubeen.mubeenmart.model;

import java.time.LocalDateTime;

/**
 * Wishlist / save-for-later item entity.
 */
public class WishlistItem {

    private Long id;
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;

    // Transient joined product
    private Product product;

    public WishlistItem() {
    }

    public WishlistItem(Long id, Long userId, Long productId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
