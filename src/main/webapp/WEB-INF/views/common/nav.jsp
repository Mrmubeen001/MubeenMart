<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="navbar">
    <div class="nav-container">
        <a href="${pageContext.request.contextPath}/home" class="brand-logo">
            🛍️ MubeenMart <span class="brand-badge">Marketplace</span>
        </a>

        <!-- Search & Filter Form -->
        <form action="${pageContext.request.contextPath}/home" method="get" class="search-bar">
            <select name="category" aria-label="Product Category">
                <option value="ALL">All Categories</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="<c:out value='${cat}' />" ${selectedCategory == cat ? 'selected' : ''}>
                        <c:out value="${cat}" />
                    </option>
                </c:forEach>
            </select>
            <input type="text" name="q" placeholder="Search products, brands, essentials..." 
                   value="<c:out value='${searchQuery}' />" aria-label="Search Query">
            <button type="submit">Search</button>
        </form>

        <div class="nav-actions">
            <a href="${pageContext.request.contextPath}/home" class="nav-link">Shop</a>

            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <c:if test="${sessionScope.currentUser.role == 'SELLER' || sessionScope.currentUser.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link" style="color: #2563eb; font-weight: 600;">
                            📊 Seller Portal
                        </a>
                    </c:if>

                    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link" style="color: #9333ea; font-weight: 600;">
                            🛡️ Admin
                        </a>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/wishlist" class="nav-link" title="Saved Wishlist">
                        🤍 Wishlist
                    </a>

                    <a href="${pageContext.request.contextPath}/orders" class="nav-link">
                        📦 Orders
                    </a>

                    <a href="${pageContext.request.contextPath}/cart" class="nav-link" title="Shopping Cart">
                        🛒 Cart <span id="navbarCartCount" class="cart-badge">${cartItems != null ? cartItems.size() : 0}</span>
                    </a>

                    <span class="nav-link" style="color: var(--text-muted); font-size: 0.85rem;">
                        👤 <c:out value="${sessionScope.currentUser.name}" />
                    </span>

                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-secondary btn-sm">Logout</a>
                </c:when>

                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/cart" class="nav-link">
                        🛒 Cart
                    </a>
                    <a href="${pageContext.request.contextPath}/login" class="nav-link">Log In</a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Sign Up</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>
