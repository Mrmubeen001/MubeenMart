<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Shopping Cart — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <h1 style="font-size: 1.75rem; font-weight: 700; margin-bottom: 0.5rem;">Shopping Cart</h1>
    <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">Review your selected items before checkout</p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${param.added eq 'true'}">
        <div class="alert alert-success">Item added to your cart successfully!</div>
    </c:if>
    <c:if test="${param.removed eq 'true'}">
        <div class="alert alert-success">Item removed from your cart.</div>
    </c:if>
    <c:if test="${param.empty eq 'true'}">
        <div class="alert alert-warning">Your cart is currently empty. Please add items before proceeding to checkout.</div>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div style="display: grid; grid-template-columns: 1fr 340px; gap: 2rem; align-items: start;">
                <!-- Cart Items Table -->
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Unit Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 0.75rem;">
                                            <img src="${item.product.imageUrl}" alt="<c:out value='${item.product.name}' />" 
                                                 style="width: 48px; height: 48px; object-fit: cover; border-radius: 4px; border: 1px solid var(--border-color);"
                                                 onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
                                            <div>
                                                <a href="${pageContext.request.contextPath}/products/${item.product.id}" style="font-weight: 600;">
                                                    <c:out value="${item.product.name}" />
                                                </a>
                                                <div style="font-size: 0.75rem; color: var(--text-muted);">
                                                    Category: <c:out value="${item.product.category}" />
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                    <td style="font-weight: 600;">
                                        ₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00" />
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart" method="post" style="display: flex; gap: 0.35rem; align-items: center;">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" max="${item.product.stockQty}" 
                                                   style="width: 55px; padding: 0.3rem; border: 1px solid var(--border-color); border-radius: 4px; text-align: center;">
                                            <button type="submit" class="btn btn-secondary btn-sm" title="Update Quantity">↻</button>
                                        </form>
                                    </td>
                                    <td style="font-weight: 700; color: var(--secondary);">
                                        ₹<fmt:formatNumber value="${item.itemTotal}" pattern="#,##0.00" />
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart" method="post" style="margin: 0;">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <button type="submit" class="btn btn-danger btn-sm" title="Remove Item">&times;</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Order Running Total Card (Requirement F4) -->
                <div style="background: #fff; border: 1px solid var(--border-color); border-radius: var(--radius); padding: 1.5rem; box-shadow: var(--shadow-sm);">
                    <h2 style="font-size: 1.2rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
                        Order Summary
                    </h2>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.9rem;">
                        <span style="color: var(--text-muted);">Items Subtotal:</span>
                        <span style="font-weight: 600;">₹<fmt:formatNumber value="${runningTotal}" pattern="#,##0.00" /></span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.9rem;">
                        <span style="color: var(--text-muted);">Standard Shipping:</span>
                        <span style="color: var(--success); font-weight: 600;">FREE</span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-top: 1rem; padding-top: 1rem; border-top: 2px dashed var(--border-color); font-size: 1.25rem;">
                        <strong>Running Total:</strong>
                        <strong style="color: var(--primary);">₹<fmt:formatNumber value="${runningTotal}" pattern="#,##0.00" /></strong>
                    </div>

                    <div style="margin-top: 1.5rem;">
                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-block" style="padding: 0.75rem; font-size: 1rem;">
                            Proceed to Checkout &rarr;
                        </a>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary btn-block" style="margin-top: 0.5rem;">
                            Continue Shopping
                        </a>
                    </div>
                </div>
            </div>
        </c:when>

        <c:otherwise>
            <div style="text-align: center; padding: 4rem 1rem; background: #fff; border: 1px solid var(--border-color); border-radius: 8px;">
                <p style="font-size: 1.5rem; margin-bottom: 0.5rem;">🛒</p>
                <h3 style="font-size: 1.2rem; font-weight: 600;">Your Cart is Empty</h3>
                <p style="color: var(--text-muted); margin-top: 0.5rem; margin-bottom: 1.5rem;">You haven't added any products to your shopping cart yet.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Start Browsing</a>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
