<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Floating AI Chatbot Assistant Widget (Phase 3 Requirement) -->
<button id="chatWidgetBtn" class="chat-widget-btn" aria-label="Open AI Assistant">
    💬
</button>

<div id="chatPanel" class="chat-panel">
    <div class="chat-header">
        <div>
            <h4>🤖 MubeenMart Assistant</h4>
            <span style="font-size: 0.7rem; color: #94a3b8;">Instant Customer Support</span>
        </div>
        <button id="chatCloseBtn" class="chat-close" aria-label="Close Assistant">&times;</button>
    </div>

    <div id="chatMessages" class="chat-messages">
        <div class="chat-msg bot">
            Hello! I am your MubeenMart AI assistant. How can I help you with products, order status, or seller registration today?
        </div>
    </div>

    <!-- Quick FAQ Suggestion Chips -->
    <div class="chat-chips">
        <button type="button" class="chip-btn" data-query="How do I track my order?">Track Order</button>
        <button type="button" class="chip-btn" data-query="What is the shipping and delivery policy?">Shipping</button>
        <button type="button" class="chip-btn" data-query="How do I become a seller?">Become a Seller</button>
        <button type="button" class="chip-btn" data-query="What mock payment methods are supported?">Payments</button>
        <button type="button" class="chip-btn" data-query="How do returns and refunds work?">Returns</button>
    </div>

    <form id="chatForm" class="chat-footer">
        <input type="text" id="chatInput" class="chat-input" placeholder="Ask about products, orders, returns..." maxlength="500" required>
        <button type="submit" class="chat-send-btn">Send</button>
    </form>
</div>
