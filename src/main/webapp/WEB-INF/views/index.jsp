<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="MubeenMart — Multi-Seller E-Commerce Marketplace" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success"><c:out value="${successMessage}" /></div>
    </c:if>

    <!-- Hero Banner -->
    <section style="background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%); color: #fff; padding: 2.5rem 2rem; border-radius: 12px; margin-bottom: 2rem; box-shadow: var(--shadow-sm);">
        <h1 style="font-size: 2.2rem; font-weight: 800; margin-bottom: 0.5rem; letter-spacing: -0.5px;">
            Shop Verified Quality on MubeenMart
        </h1>
        <p style="color: #94a3b8; font-size: 1.05rem; max-width: 650px; margin-bottom: 1.5rem;">
            Explore electronics, fashion, study handbooks, and home goods directly from independent sellers with transparent reviews and instant mock checkout.
        </p>
        <div style="display: flex; gap: 0.75rem; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Browse All Products</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary" style="background: rgba(255,255,255,0.1); color: #fff; border-color: rgba(255,255,255,0.2);">Sell on MubeenMart</a>
        </div>
    </section>

    <!-- Category Filter Bar (Requirement F3) -->
    <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.5rem;">
        <div>
            <h2 style="font-size: 1.35rem; font-weight: 700;">
                <c:choose>
                    <c:when test="${not empty searchQuery}">
                        Search results for "<c:out value='${searchQuery}' />"
                    </c:when>
                    <c:when test="${not empty selectedCategory && selectedCategory != 'ALL'}">
                        Category: <c:out value='${selectedCategory}' />
                    </c:when>
                    <c:otherwise>
                        Explore Marketplace Products
                    </c:otherwise>
                </c:choose>
            </h2>
            <p style="color: var(--text-muted); font-size: 0.875rem;">Showing ${products.size()} available listing(s)</p>
        </div>

        <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
            <a href="${pageContext.request.contextPath}/home" 
               class="btn ${empty selectedCategory || selectedCategory == 'ALL' ? 'btn-primary' : 'btn-secondary'} btn-sm">
                All
            </a>
            <c:forEach var="cat" items="${categories}">
                <a href="${pageContext.request.contextPath}/home?category=<c:out value='${cat}' />" 
                   class="btn ${selectedCategory == cat ? 'btn-primary' : 'btn-secondary'} btn-sm">
                    <c:out value="${cat}" />
                </a>
            </c:forEach>
        </div>
    </div>

    <!-- Product Grid -->
    <div class="product-grid">
        <c:forEach var="p" items="${products}">
            <div class="product-card">
                <a href="${pageContext.request.contextPath}/products/${p.id}">
                    <img src="${p.imageUrl}" alt="<c:out value='${p.name}' />" class="product-image" 
                         onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
                </a>
                <div class="product-body">
                    <span class="product-category"><c:out value="${p.category}" /></span>
                    <h3 class="product-title">
                        <a href="${pageContext.request.contextPath}/products/${p.id}">
                            <c:out value="${p.name}" />
                        </a>
                    </h3>

                    <div class="product-rating">
                        <span>★</span>
                        <strong><fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1" minFractionDigits="1" /></strong>
                        <span style="color: var(--text-muted); font-size: 0.75rem;">(${p.reviewCount})</span>
                    </div>

                    <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.5rem;">
                        Seller: <strong><c:out value="${p.sellerName}" /></strong>
                    </div>

                    <div style="display: flex; justify-content: space-between; align-items: baseline;">
                        <span class="product-price">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></span>
                        <c:choose>
                            <c:when test="${p.stockQty > 0}">
                                <span style="font-size: 0.75rem; color: var(--success); font-weight: 600;">In Stock (${p.stockQty})</span>
                            </c:when>
                            <c:otherwise>
                                <span style="font-size: 0.75rem; color: var(--danger); font-weight: 600;">Out of Stock</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="product-footer">
                    <c:choose>
                        <c:when test="${p.stockQty > 0}">
                            <button type="button" class="btn btn-primary btn-sm btn-block ajax-add-cart" 
                                    data-product-id="${p.id}">
                                🛒 Add to Cart
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" class="btn btn-secondary btn-sm btn-block" disabled>
                                Sold Out
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <form action="${pageContext.request.contextPath}/wishlist/add" method="post" style="margin: 0;">
                        <input type="hidden" name="productId" value="${p.id}">
                        <button type="submit" class="btn btn-secondary btn-sm" title="Save to Wishlist">
                            🤍
                        </button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty products}">
        <div style="text-align: center; padding: 4rem 1rem; background: #fff; border-radius: 8px; border: 1px solid var(--border-color); margin-top: 1.5rem;">
            <p style="font-size: 1.2rem; font-weight: 600; color: var(--text-main);">No products found matching your criteria</p>
            <p style="color: var(--text-muted); margin-top: 0.5rem;">Try selecting a different category or clearing search keywords.</p>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-primary" style="margin-top: 1rem;">View All Catalog</a>
        </div>
    </c:if>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
