package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.service.ProductService;
import com.mubeen.mubeenmart.service.impl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller servlet for the home page and marketplace storefront.
 */
@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(HomeServlet.class);
    private final ProductService productService;

    public HomeServlet() {
        this(new ProductServiceImpl());
    }

    public HomeServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String category = request.getParameter("category");
            String query = request.getParameter("q");

            List<Product> products;
            if ((category != null && !category.isEmpty()) || (query != null && !query.isEmpty())) {
                products = productService.searchProducts(category, query);
            } else {
                products = productService.getAllProducts();
            }

            List<String> categories = productService.getCategories();

            request.setAttribute("products", products);
            request.setAttribute("categories", categories);
            request.setAttribute("selectedCategory", category);
            request.setAttribute("searchQuery", query);

            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        } catch (Exception e) {
            logger.error("Failed to render marketplace home page", e);
            request.setAttribute("errorMessage", "Unable to load product listings. Please refresh.");
            request.setAttribute("products", java.util.Collections.emptyList());
            request.setAttribute("categories", java.util.Collections.emptyList());
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        }
    }
}
