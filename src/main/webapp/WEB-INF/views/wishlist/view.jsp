<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="My Wishlist — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <h1 style="font-size: 1.75rem; font-weight: 700; margin-bottom: 0.5rem;">Saved Wishlist</h1>
    <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">
        Your personal save-for-later collection (Requirement O1)
    </p>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${param.added eq 'true'}">
        <div class="alert alert-success">Item saved to your wishlist!</div>
    </c:if>
    <c:if test="${param.removed eq 'true'}">
        <div class="alert alert-success">Item removed from your wishlist.</div>
    </c:if>

    <div class="product-grid">
        <c:forEach var="item" items="${wishlistItems}">
            <div class="product-card">
                <a href="${pageContext.request.contextPath}/products/${item.product.id}">
                    <img src="${item.product.imageUrl}" alt="<c:out value='${item.product.name}' />" class="product-image"
                         onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
                </a>
                <div class="product-body">
                    <span class="product-category"><c:out value="${item.product.category}" /></span>
                    <h3 class="product-title">
                        <a href="${pageContext.request.contextPath}/products/${item.product.id}">
                            <c:out value="${item.product.name}" />
                        </a>
                    </h3>
                    <div class="product-price">₹<fmt:formatNumber value="${item.product.price}" pattern="#,##0.00" /></div>
                </div>

                <div class="product-footer">
                    <c:choose>
                        <c:when test="${item.product.stockQty > 0}">
                            <form action="${pageContext.request.contextPath}/cart" method="post" style="flex: 1; margin: 0;">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${item.product.id}">
                                <input type="hidden" name="quantity" value="1">
                                <button type="submit" class="btn btn-primary btn-sm btn-block">🛒 Move to Cart</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-secondary btn-sm btn-block" disabled>Out of Stock</button>
                        </c:otherwise>
                    </c:choose>

                    <form action="${pageContext.request.contextPath}/wishlist/remove" method="post" style="margin: 0;">
                        <input type="hidden" name="productId" value="${item.product.id}">
                        <button type="submit" class="btn btn-danger btn-sm" title="Remove from Wishlist">&times;</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty wishlistItems}">
        <div style="text-align: center; padding: 4rem 1rem; background: #fff; border: 1px solid var(--border-color); border-radius: 8px;">
            <p style="font-size: 1.5rem; margin-bottom: 0.5rem;">🤍</p>
            <h3 style="font-size: 1.2rem; font-weight: 600;">Your Wishlist is Empty</h3>
            <p style="color: var(--text-muted); margin-top: 0.5rem; margin-bottom: 1.5rem;">Save items here by clicking the heart button on any product.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Browse Marketplace</a>
        </div>
    </c:if>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
