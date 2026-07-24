package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.OrderService;
import com.mubeen.mubeenmart.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller servlet for customer order history and order tracking.
 * Fulfills Requirement F6: "Order history: buyer view of past orders and status."
 */
@WebServlet(urlPatterns = {"/orders", "/orders/*"})
public class OrderServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(OrderServlet.class);
    private final OrderService orderService;

    public OrderServlet() {
        this(new OrderServiceImpl());
    }

    public OrderServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/orders");
            return;
        }

        String pathInfo = request.getPathInfo();
        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                // View specific order detail
                Long orderId = Long.parseLong(pathInfo.substring(1));
                Order order = orderService.getOrderById(orderId, user.getId(), user.getRole());
                request.setAttribute("order", order);
                request.getRequestDispatcher("/WEB-INF/views/orders/detail.jsp").forward(request, response);
            } else {
                // View buyer order history list
                List<Order> orders = orderService.getBuyerOrders(user.getId());
                request.setAttribute("orders", orders);
                if (request.getParameter("placedOrderId") != null) {
                    request.setAttribute("successMessage", "Order #" + request.getParameter("placedOrderId") +
                            " placed successfully! Mock payment was authorized.");
                }
                request.getRequestDispatcher("/WEB-INF/views/orders/history.jsp").forward(request, response);
            }
        } catch (AppException e) {
            logger.error("Error loading order for user: {}", user.getId(), e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/orders/history.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order number");
        }
    }
}
