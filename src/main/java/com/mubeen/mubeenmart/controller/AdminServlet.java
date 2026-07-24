package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.UserResponseDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.OrderStatus;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.OrderService;
import com.mubeen.mubeenmart.service.ProductService;
import com.mubeen.mubeenmart.service.UserService;
import com.mubeen.mubeenmart.service.impl.OrderServiceImpl;
import com.mubeen.mubeenmart.service.impl.ProductServiceImpl;
import com.mubeen.mubeenmart.service.impl.UserServiceImpl;
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
 * Controller servlet for administrator operations.
 * Fulfills Requirement F7: "Admin: view all users and orders; moderate/remove listings."
 */
@WebServlet(urlPatterns = {"/admin", "/admin/*"})
public class AdminServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(AdminServlet.class);

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminServlet() {
        this(new UserServiceImpl(), new ProductServiceImpl(), new OrderServiceImpl());
    }

    public AdminServlet(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/admin/dashboard");
            return;
        }

        try {
            List<UserResponseDTO> allUsers = userService.getAllUsers();
            List<Order> allOrders = orderService.getAllOrders();
            List<Product> allProducts = productService.getAllProducts();

            BigDecimal totalMarketplaceVolume = BigDecimal.ZERO;
            for (Order o : allOrders) {
                totalMarketplaceVolume = totalMarketplaceVolume.add(o.getTotalAmount());
            }

            request.setAttribute("users", allUsers);
            request.setAttribute("orders", allOrders);
            request.setAttribute("products", allProducts);
            request.setAttribute("totalMarketplaceVolume", totalMarketplaceVolume);

            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } catch (AppException e) {
            logger.error("Failed to load admin dashboard", e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        try {
            if ("/products/delete".equals(pathInfo)) {
                // F7: Moderate/remove listing
                Long productId = Long.parseLong(request.getParameter("productId"));
                productService.deleteProduct(user.getId(), productId, true);
                logger.info("Admin {} moderated product ID: {}", user.getEmail(), productId);
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?productDeleted=true");

            } else if ("/orders/status".equals(pathInfo)) {
                // Admin status override
                Long orderId = Long.parseLong(request.getParameter("orderId"));
                OrderStatus newStatus = OrderStatus.fromString(request.getParameter("status"));
                orderService.updateOrderStatus(orderId, newStatus, user.getId(), user.getRole());
                response.sendRedirect(request.getContextPath() + "/admin/dashboard?orderUpdated=true");

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown admin command");
            }
        } catch (AppException e) {
            logger.warn("Admin operation error: {}", e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            logger.error("Error executing admin action", e);
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            doGet(request, response);
        }
    }
}
