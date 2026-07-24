package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.dto.OrderRequestDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.CartItem;
import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.CartService;
import com.mubeen.mubeenmart.service.OrderService;
import com.mubeen.mubeenmart.service.impl.CartServiceImpl;
import com.mubeen.mubeenmart.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller servlet for checkout and mock payment processing.
 * Fulfills Requirement F5: "Checkout: place an order from cart contents via mock payment confirmation."
 */
@WebServlet("/checkout")
public class CheckoutServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutServlet.class);
    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutServlet() {
        this(new CartServiceImpl(), new OrderServiceImpl());
    }

    public CheckoutServlet(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        try {
            List<CartItem> items = cartService.getCart(user.getId());
            if (items.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/cart?empty=true");
                return;
            }

            BigDecimal runningTotal = cartService.calculateRunningTotal(items);
            request.setAttribute("cartItems", items);
            request.setAttribute("runningTotal", runningTotal);

            request.getRequestDispatcher("/WEB-INF/views/checkout/checkout.jsp").forward(request, response);
        } catch (AppException e) {
            logger.error("Failed to load checkout page for user: {}", user.getId(), e);
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_UNAUTHORIZED,
                        ApiResponse.error("UNAUTHORIZED", "Please log in to complete checkout"));
            } else {
                response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            }
            return;
        }

        OrderRequestDTO dto;
        if (isJsonRequest(request)) {
            dto = readJsonBody(request, OrderRequestDTO.class);
        } else {
            dto = new OrderRequestDTO(
                    request.getParameter("shippingAddress"),
                    request.getParameter("paymentMethod")
            );
        }

        try {
            Order placedOrder = orderService.checkout(user.getId(), dto);

            if (isJsonRequest(request)) {
                sendJsonResponse(response, HttpServletResponse.SC_CREATED, ApiResponse.success(placedOrder));
            } else {
                response.sendRedirect(request.getContextPath() + "/orders?placedOrderId=" + placedOrder.getId());
            }
        } catch (AppException e) {
            logger.warn("Checkout placement failed for buyer {}: {}", user.getId(), e.getMessage());
            if (isJsonRequest(request)) {
                sendJsonResponse(response, e.getStatusCode(), ApiResponse.error("CHECKOUT_ERROR", e.getMessage()));
            } else {
                request.setAttribute("errorMessage", e.getMessage());
                doGet(request, response);
            }
        }
    }
}
