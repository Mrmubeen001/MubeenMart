package com.mubeen.mubeenmart.dao;

import com.mubeen.mubeenmart.dao.impl.*;

/**
 * Factory pattern implementation for providing DAO instances.
 * Satisfies Section 12 requirement: "Factory (DAO/provider instantiation)".
 */
public final class DAOFactory {

    private static final UserDAO USER_DAO = new UserDAOImpl();
    private static final ProductDAO PRODUCT_DAO = new ProductDAOImpl();
    private static final OrderDAO ORDER_DAO = new OrderDAOImpl();
    private static final OrderItemDAO ORDER_ITEM_DAO = new OrderItemDAOImpl();
    private static final CartDAO CART_DAO = new CartDAOImpl();
    private static final ReviewDAO REVIEW_DAO = new ReviewDAOImpl();
    private static final WishlistDAO WISHLIST_DAO = new WishlistDAOImpl();

    private DAOFactory() {
        // Prevent instantiation
    }

    public static UserDAO getUserDAO() {
        return USER_DAO;
    }

    public static ProductDAO getProductDAO() {
        return PRODUCT_DAO;
    }

    public static OrderDAO getOrderDAO() {
        return ORDER_DAO;
    }

    public static OrderItemDAO getOrderItemDAO() {
        return ORDER_ITEM_DAO;
    }

    public static CartDAO getCartDAO() {
        return CART_DAO;
    }

    public static ReviewDAO getReviewDAO() {
        return REVIEW_DAO;
    }

    public static WishlistDAO getWishlistDAO() {
        return WISHLIST_DAO;
    }
}
