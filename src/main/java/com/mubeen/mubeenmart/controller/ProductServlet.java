package com.mubeen.mubeenmart.controller;

import com.mubeen.mubeenmart.model.Product;
import com.mubeen.mubeenmart.model.Review;
import com.mubeen.mubeenmart.model.User;
import com.mubeen.mubeenmart.service.ProductService;
import com.mubeen.mubeenmart.service.ReviewService;
import com.mubeen.mubeenmart.service.WishlistService;
import com.mubeen.mubeenmart.service.impl.ProductServiceImpl;
import com.mubeen.mubeenmart.service.impl.ReviewServiceImpl;
import com.mubeen.mubeenmart.service.impl.WishlistServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller servlet for viewing product details and catalog navigation.
 */
@WebServlet(urlPatterns = {"/products", "/products/*"})
public class ProductServlet extends BaseServlet {

    private static final Logger logger = LoggerFactory.getLogger(ProductServlet.class);
    private final ProductService productService;
    private final ReviewService reviewService;
    private final WishlistService wishlistService;

    public ProductServlet() {
        this(new ProductServiceImpl(), new ReviewServiceImpl(), new WishlistServiceImpl());
    }

    public ProductServlet(ProductService productService, ReviewService reviewService, WishlistService wishlistService) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.wishlistService = wishlistService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Forward to home page with search/filters if any
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            Long productId = Long.parseLong(pathInfo.substring(1));
            Product product = productService.getProductById(productId);
            List<Review> reviews = reviewService.getReviewsForProduct(productId);

            User user = getCurrentUser(request);
            boolean eligibleToReview = false;
            boolean inWishlist = false;

            if (user != null) {
                eligibleToReview = reviewService.isEligibleToReview(user.getId(), productId);
                inWishlist = wishlistService.isInWishlist(user.getId(), productId);
            }

            request.setAttribute("product", product);
            request.setAttribute("reviews", reviews);
            request.setAttribute("eligibleToReview", eligibleToReview);
            request.setAttribute("inWishlist", inWishlist);

            request.getRequestDispatcher("/WEB-INF/views/products/detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product identifier");
        } catch (Exception e) {
            logger.error("Error retrieving product details", e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
        }
    }
}
