package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.CartItem;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.CartService;
import com.mubeen.mubeenmart.service.impl.CartServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Controller servlet for cart operations: view, add, update, and remove.
 * Fulfills Requirement F4: "Cart: add, update, remove items; display running total."
 */
@WebServlet(urlPatterns = {"/cart", "/cart/*"})
public class CartServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(CartServlet.class);
    private final CartService cartService;

    public CartServlet() {
        this(new CartServiceImpl());
    }

    public CartServlet(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
            return;
        }

        try {
            List<CartItem> items = cartService.getCart(user.getId());
            BigDecimal runningTotal = cartService.calculateRunningTotal(items);

            request.setAttribute("cartItems", items);
            request.setAttribute("runningTotal", runningTotal);

            request.getRequestDispatcher("/WEB-INF/views/cart/view.jsp").forward(request, response);
        } catch (AppException e) {
            logger.error("Failed to load cart for user: {}", user.getId(), e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/cart/view.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Please log in to manage your shopping cart"));
            } else {
                response.sendRedirect(request.getContextPath() + "/login?redirect=/cart");
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
                int quantity = 1;
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                CartItem item = cartService.addToCart(user.getId(), productId, quantity);

                if (isJsonRequest(request)) {
                    List<CartItem> items = cartService.getCart(user.getId());
                    BigDecimal total = cartService.calculateRunningTotal(items);
                    sendJsonResponse(response, HttpServletResponse.SC_OK,
                            ApiResponse.success(Map.of("item", item, "cartCount", items.size(), "runningTotal", total)));
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart?added=true");
                }

            } else if ("/update".equals(action) || "update".equals(action)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cartService.updateQuantity(user.getId(), cartItemId, quantity);

                if (isJsonRequest(request)) {
                    List<CartItem> items = cartService.getCart(user.getId());
                    BigDecimal total = cartService.calculateRunningTotal(items);
                    sendJsonResponse(response, HttpServletResponse.SC_OK,
                            ApiResponse.success(Map.of("cartCount", items.size(), "runningTotal", total)));
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart");
                }

            } else if ("/remove".equals(action) || "remove".equals(action)) {
                Long cartItemId = Long.parseLong(request.getParameter("cartItemId"));
                cartService.removeFromCart(user.getId(), cartItemId);

                if (isJsonRequest(request)) {
                    List<CartItem> items = cartService.getCart(user.getId());
                    BigDecimal total = cartService.calculateRunningTotal(items);
                    sendJsonResponse(response, HttpServletResponse.SC_OK,
                            ApiResponse.success(Map.of("cartCount", items.size(), "runningTotal", total)));
                } else {
                    response.sendRedirect(request.getContextPath() + "/cart?removed=true");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown cart action");
            }
        } catch (AppException e) {
            logger.warn("Cart operation failed for user {}: {}", user.getId(), e.getMessage());
            if (isJsonRequest(request)) {
                sendJsonResponse(response, e.getStatusCode(), ApiResponse.error("CART_ERROR", e.getMessage()));
            } else {
                request.setAttribute("errorMessage", e.getMessage());
                doGet(request, response);
            }
        }
    }
}
