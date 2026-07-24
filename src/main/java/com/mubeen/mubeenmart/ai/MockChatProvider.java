package com.mubeen.mubeenmart.ai;

import java.util.Locale;

/**
 * Mock AI Chat Provider implementing canned domain FAQ answers with zero network latency.
 * Conforms to Section 11 & 17 requirements for offline evaluation and testing.
 */
public class MockChatProvider implements ChatProvider {

    @Override
    public String getReply(String userMessage, String context) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! I am your MubeenMart Assistant. How can I help you find products, track orders, or sell items today?";
        }

        String msg = userMessage.toLowerCase(Locale.ROOT).trim();

        // 1. Order Tracking & Status
        if (msg.contains("order") && (msg.contains("track") || msg.contains("status") || msg.contains("where"))) {
            return "To track your order, visit the 'My Orders' tab in the navigation menu. Orders transition through: Pending → Confirmed → Shipped → Delivered. Click on any order to see full item breakdown and delivery progress.";
        }

        // 2. Shipping & Delivery
        if (msg.contains("shipping") || msg.contains("delivery") || msg.contains("deliver")) {
            return "Standard delivery takes 2 to 4 business days nationwide. Sellers dispatch orders within 24 hours of confirmation. You will receive real-time status updates in your order dashboard.";
        }

        // 3. Payment Methods
        if (msg.contains("payment") || msg.contains("pay") || msg.contains("upi") || msg.contains("card")) {
            return "MubeenMart supports secure mock checkout options including Instant UPI (Google Pay, PhonePe, Paytm), Credit/Debit Cards (Visa, MasterCard, RuPay), and Net Banking across all major banks.";
        }

        // 4. Returns & Refunds
        if (msg.contains("return") || msg.contains("refund") || msg.contains("cancel")) {
            return "You can request a hassle-free cancellation while an order is in 'Pending' status. Once delivered, MubeenMart offers a 7-day replacement guarantee for damaged or defective items.";
        }

        // 5. How to Sell / Become a Seller
        if (msg.contains("sell") || msg.contains("seller") || msg.contains("merchant") || msg.contains("vendor")) {
            return "To start selling on MubeenMart: 1) Register an account and choose the 'Seller' role. 2) Log in to access your Seller Portal. 3) Click 'Add Product' to list your items with photos, price, category, and inventory count!";
        }

        // 6. Reviews & Ratings
        if (msg.contains("review") || msg.contains("rating") || msg.contains("star")) {
            return "Buyers can leave verified star ratings (1 to 5 stars) and detailed comments on products from orders that have been marked as 'Delivered'. This ensures transparent and trustworthy feedback.";
        }

        // 7. Wishlist / Save for Later
        if (msg.contains("wishlist") || msg.contains("save") || msg.contains("later")) {
            return "You can click the heart icon on any product card or detail page to add items to your Wishlist. Access your saved items anytime from the 'Wishlist' link in the top navigation.";
        }

        // 8. Categories & Available Products
        if (msg.contains("category") || msg.contains("categories") || msg.contains("electronics") || msg.contains("book") || msg.contains("fashion")) {
            return "MubeenMart features curated items across Electronics, Fashion, Home & Living, and Academic Books. Use the category dropdown and search bar on the homepage to find what you need!";
        }

        // 9. Buyer Protection & Security
        if (msg.contains("secure") || msg.contains("safety") || msg.contains("safe") || msg.contains("password")) {
            return "Your account is secured with industrial-grade bcrypt encryption and strict session protection. All transactions use parameterized SQL queries to safeguard your information.";
        }

        // 10. Contact / Support
        if (msg.contains("contact") || msg.contains("support") || msg.contains("help") || msg.contains("admin")) {
            return "For support, you can contact the MubeenMart admin team at support@mubeenmart.com or reach our student project lead Mohamed Mubeen.";
        }

        // Domain context fallback
        return "I can help with questions about MubeenMart products, order tracking, shipping, returns, mock payment, or how to sell. Feel free to ask about any of these topics!";
    }
}
