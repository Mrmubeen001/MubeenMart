<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Checkout & Payment — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <h1 style="font-size: 1.75rem; font-weight: 700; margin-bottom: 0.5rem;">Checkout & Payment</h1>
    <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">
        Complete your order with secure payment confirmation
    </p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>

    <div style="display: grid; grid-template-columns: 1fr 380px; gap: 2rem; align-items: start;">
        <!-- Shipping & Payment Form -->
        <div style="background: #fff; padding: 2rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm);">
            <form action="${pageContext.request.contextPath}/checkout" method="post">
                <h2 style="font-size: 1.25rem; font-weight: 600; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
                    1. Shipping Information
                </h2>

                <div class="form-group">
                    <label for="shippingAddress" class="form-label">Delivery Street Address & Postal Code</label>
                    <textarea id="shippingAddress" name="shippingAddress" class="form-control" rows="3" required
                              placeholder="e.g. 42 Anna Salai, Guindy, Chennai, Tamil Nadu 600025"></textarea>
                </div>

                <h2 style="font-size: 1.25rem; font-weight: 600; margin-top: 2rem; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
                    2. Select Payment Method
                </h2>

                <div style="display: flex; flex-direction: column; gap: 0.75rem; margin-bottom: 1.5rem;">
                    <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 1rem; border: 1px solid var(--border-color); border-radius: 8px; cursor: pointer; background: #f8fafc;">
                        <input type="radio" name="paymentMethod" value="MOCK_UPI" checked style="margin-top: 3px;">
                        <div>
                            <strong style="display: block;">📱 Instant UPI</strong>
                            <span style="font-size: 0.8rem; color: var(--text-muted);">Instant authorization via Google Pay, PhonePe, or BHIM UPI</span>
                        </div>
                    </label>

                    <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 1rem; border: 1px solid var(--border-color); border-radius: 8px; cursor: pointer;">
                        <input type="radio" name="paymentMethod" value="MOCK_CARD" style="margin-top: 3px;">
                        <div>
                            <strong style="display: block;">💳 Credit / Debit Card</strong>
                            <span style="font-size: 0.8rem; color: var(--text-muted);">Secure card gateway authorization (Visa / MasterCard / RuPay)</span>
                        </div>
                    </label>

                    <label style="display: flex; align-items: flex-start; gap: 0.75rem; padding: 1rem; border: 1px solid var(--border-color); border-radius: 8px; cursor: pointer;">
                        <input type="radio" name="paymentMethod" value="MOCK_NET_BANKING" style="margin-top: 3px;">
                        <div>
                            <strong style="display: block;">🏦 Net Banking</strong>
                            <span style="font-size: 0.8rem; color: var(--text-muted);">Direct bank transfer settlement</span>
                        </div>
                    </label>
                </div>

                <div style="background: #f0fdf4; border: 1px solid #bbf7d0; padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem; font-size: 0.85rem; color: #166534;">
                    🛡️ <strong>Secure Sandbox Mode:</strong> Real money is not charged. Submitting processes simulated payment authorization and confirms your order.
                </div>

                <button type="submit" class="btn btn-primary btn-block" style="padding: 0.85rem; font-size: 1.05rem;">
                    ✓ Confirm Payment & Place Order (₹<fmt:formatNumber value="${runningTotal}" pattern="#,##0.00" />)
                </button>
            </form>
        </div>

        <!-- Order Items Summary Sidebar -->
        <div style="background: #fff; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm);">
            <h3 style="font-size: 1.1rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
                Order Items (${cartItems.size()})
            </h3>

            <div style="display: flex; flex-direction: column; gap: 0.75rem; margin-bottom: 1rem;">
                <c:forEach var="item" items="${cartItems}">
                    <div style="display: flex; justify-content: space-between; align-items: center; font-size: 0.875rem;">
                        <div style="max-width: 200px;">
                            <div style="font-weight: 600;"><c:out value="${item.product.name}" /></div>
                            <span style="color: var(--text-muted); font-size: 0.75rem;">Qty: ${item.quantity} &times; ₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00" /></span>
                        </div>
                        <span style="font-weight: 600;">₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00" /></span>
                    </div>
                </c:forEach>
            </div>

            <div style="border-top: 2px dashed var(--border-color); padding-top: 1rem; margin-top: 1rem; display: flex; justify-content: space-between; font-size: 1.2rem;">
                <strong>Total Amount:</strong>
                <strong style="color: var(--primary);">₹<fmt:formatNumber value="${runningTotal}" pattern="#,##0.00" /></strong>
            </div>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
