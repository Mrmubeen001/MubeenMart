package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.model.WishlistItem;
import com.mubeen.mubeenmart.service.WishlistService;
import com.mubeen.mubeenmart.service.impl.WishlistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Controller servlet for customer wishlist / save-for-later functionality (O1).
 */
@WebServlet(urlPatterns = {"/wishlist", "/wishlist/*"})
public class WishlistServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(WishlistServlet.class);
    private final WishlistService wishlistService;

    public WishlistServlet() {
        this(new WishlistServiceImpl());
    }

    public WishlistServlet(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/wishlist");
            return;
        }

        try {
            List<WishlistItem> items = wishlistService.getWishlist(user.getId());
            request.setAttribute("wishlistItems", items);
            request.getRequestDispatcher("/WEB-INF/views/wishlist/view.jsp").forward(request, response);
        } catch (AppException e) {
            logger.error("Failed to load wishlist for user: {}", user.getId(), e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/wishlist/view.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Please log in to manage your wishlist"));
            } else {
                response.sendRedirect(request.getContextPath() + "/login");
            }
            return;
        }

        String action = request.getPathInfo();
        if (action == null) {
            action = request.getParameter("action");
        }

        try {
            if ("/add".equals(action) || "add".equals(action)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                WishlistItem item = wishlistService.addToWishlist(user.getId(), productId);

                if (isJsonRequest(request)) {
                    sendJsonResponse(response, HttpServletResponse.SC_OK,
                            ApiResponse.success(Map.of("message", "Added to wishlist", "item", item)));
                } else {
                    response.sendRedirect(request.getContextPath() + "/wishlist?added=true");
                }
            } else if ("/remove".equals(action) || "remove".equals(action)) {
                Long productId = Long.parseLong(request.getParameter("productId"));
                wishlistService.removeFromWishlist(user.getId(), productId);

                if (isJsonRequest(request)) {
                    sendJsonResponse(response, HttpServletResponse.SC_OK,
                            ApiResponse.success(Map.of("message", "Removed from wishlist")));
                } else {
                    response.sendRedirect(request.getContextPath() + "/wishlist?removed=true");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid wishlist action");
            }
        } catch (AppException e) {
            logger.warn("Wishlist error for user {}: {}", user.getId(), e.getMessage());
            if (isJsonRequest(request)) {
                sendJsonResponse(response, e.getStatusCode(), ApiResponse.error("WISHLIST_ERROR", e.getMessage()));
            } else {
                request.setAttribute("errorMessage", e.getMessage());
                doGet(request, response);
            }
        }
    }
}
