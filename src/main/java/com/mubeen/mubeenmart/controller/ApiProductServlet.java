package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.dto.ApiResponse;
import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.service.ProductService;
import com.mubeen.mubeenmart.service.impl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * REST endpoint for products catalog under /api/v1/products.
 */
@WebServlet("/api/v1/products")
public class ApiProductServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(ApiProductServlet.class);
    private final ProductService productService;

    public ApiProductServlet() {
        this(new ProductServiceImpl());
    }

    public ApiProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String category = request.getParameter("category");
        String query = request.getParameter("q");

        try {
            List<Product> products;
            if ((category != null && !category.isEmpty()) || (query != null && !query.isEmpty())) {
                products = productService.searchProducts(category, query);
            } else {
                products = productService.getAllProducts();
            }
            sendJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(products));
        } catch (Exception e) {
            logger.error("Error retrieving products API list", e);
            sendJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("INTERNAL_ERROR", "Failed to retrieve products"));
        }
    }
}
