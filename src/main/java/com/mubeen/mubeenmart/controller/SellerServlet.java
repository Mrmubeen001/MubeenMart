package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ProductRequestDTO;
import com.mubeen.mubeenmart.dto.SalesSummaryDTO;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.model.Order;
import com.mubeen.mubeenmart.model.OrderStatus;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.OrderService;
import com.mubeen.mubeenmart.service.ProductService;
import com.mubeen.mubeenmart.service.impl.OrderServiceImpl;
import com.mubeen.mubeenmart.service.impl.ProductServiceImpl;
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
 * Controller servlet for the seller portal, fulfilling:
 * - F2: Seller listing management (create, edit, delete)
 * - F6: Seller incoming orders view
 * - O2: Order status transition workflow
 * - O3: Seller sales dashboard (orders, units, revenue)
 */
@WebServlet(urlPatterns = {"/seller", "/seller/*"})
public class SellerServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(SellerServlet.class);
    private final ProductService productService;
    private final OrderService orderService;

    public SellerServlet() {
        this(new ProductServiceImpl(), new OrderServiceImpl());
    }

    public SellerServlet(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/seller/dashboard");
            return;
        }

        try {
            // Retrieve seller metrics, listings, and incoming orders
            SalesSummaryDTO summary = orderService.getSellerSalesSummary(user.getId());
            List<Product> products = productService.getProductsBySeller(user.getId());
            List<Order> incomingOrders = orderService.getSellerOrders(user.getId());
            List<String> categories = productService.getCategories();

            request.setAttribute("summary", summary);
            request.setAttribute("products", products);
            request.setAttribute("incomingOrders", incomingOrders);
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(request, response);
        } catch (AppException e) {
            logger.error("Failed to render seller dashboard for seller: {}", user.getId(), e);
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/seller/dashboard.jsp").forward(request, response);
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
            if ("/products/create".equals(pathInfo)) {
                // F2: Create listing
                ProductRequestDTO dto = new ProductRequestDTO(
                        request.getParameter("name"),
                        request.getParameter("description"),
                        new BigDecimal(request.getParameter("price")),
                        Integer.parseInt(request.getParameter("stockQty")),
                        request.getParameter("category"),
                        request.getParameter("imageUrl")
                );
                productService.createProduct(user.getId(), dto);
                response.sendRedirect(request.getContextPath() + "/seller/dashboard?productCreated=true");

            } else if ("/products/edit".equals(pathInfo)) {
                // F2: Edit listing
                Long productId = Long.parseLong(request.getParameter("productId"));
                ProductRequestDTO dto = new ProductRequestDTO(
                        request.getParameter("name"),
                        request.getParameter("description"),
                        new BigDecimal(request.getParameter("price")),
                        Integer.parseInt(request.getParameter("stockQty")),
                        request.getParameter("category"),
                        request.getParameter("imageUrl")
                );
                productService.updateProduct(user.getId(), productId, dto, false);
                response.sendRedirect(request.getContextPath() + "/seller/dashboard?productUpdated=true");

            } else if ("/products/delete".equals(pathInfo)) {
                // F2: Delete listing
                Long productId = Long.parseLong(request.getParameter("productId"));
                productService.deleteProduct(user.getId(), productId, false);
                response.sendRedirect(request.getContextPath() + "/seller/dashboard?productDeleted=true");

            } else if ("/orders/status".equals(pathInfo)) {
                // O2: Order status workflow
                Long orderId = Long.parseLong(request.getParameter("orderId"));
                OrderStatus newStatus = OrderStatus.fromString(request.getParameter("status"));
                orderService.updateOrderStatus(orderId, newStatus, user.getId(), user.getRole());
                response.sendRedirect(request.getContextPath() + "/seller/dashboard?orderUpdated=true");

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown seller action");
            }
        } catch (AppException e) {
            logger.warn("Seller action error: {}", e.getMessage());
            request.setAttribute("errorMessage", e.getMessage());
            doGet(request, response);
        } catch (Exception e) {
            logger.error("Unexpected error in seller servlet", e);
            request.setAttribute("errorMessage", "Invalid form submission: " + e.getMessage());
            doGet(request, response);
        }
    }
}
