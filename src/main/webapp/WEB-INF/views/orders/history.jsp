<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="My Orders — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <h1 style="font-size: 1.75rem; font-weight: 700; margin-bottom: 0.5rem;">My Orders</h1>
    <p style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 1.5rem;">
        Track delivery progress and review past purchases (Requirement F6)
    </p>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success"><c:out value="${successMessage}" /></div>
    </c:if>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>

    <div style="display: flex; flex-direction: column; gap: 1.5rem;">
        <c:forEach var="order" items="${orders}">
            <div style="background: #fff; border: 1px solid var(--border-color); border-radius: var(--radius); overflow: hidden; box-shadow: var(--shadow-sm);">
                <!-- Order Header -->
                <div style="background: #f8fafc; padding: 1rem 1.5rem; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-color); flex-wrap: wrap; gap: 1rem;">
                    <div style="display: flex; gap: 2rem;">
                        <div>
                            <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Order Placed</span>
                            <div style="font-size: 0.9rem; font-weight: 600;">${order.createdAt}</div>
                        </div>
                        <div>
                            <span style="font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; font-weight: 600;">Total</span>
                            <div style="font-size: 0.9rem; font-weight: 700; color: var(--secondary);">
                                ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" />
                            </div>
                        </div>
                    </div>

                    <div style="display: flex; align-items: center; gap: 1rem;">
                        <span class="badge badge-${order.status.name().toLowerCase()}">
                            <c:out value="${order.status}" />
                        </span>
                        <a href="${pageContext.request.contextPath}/orders/${order.id}" class="btn btn-secondary btn-sm">
                            View Order Details
                        </a>
                    </div>
                </div>

                <!-- Line Items in Order -->
                <div style="padding: 1.5rem;">
                    <div style="display: flex; flex-direction: column; gap: 1rem;">
                        <c:forEach var="item" items="${order.items}">
                            <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem;">
                                <div style="display: flex; align-items: center; gap: 1rem;">
                                    <img src="${item.productImageUrl}" alt="<c:out value='${item.productName}' />" 
                                         style="width: 55px; height: 55px; object-fit: cover; border-radius: 4px; border: 1px solid var(--border-color);"
                                         onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
                                    <div>
                                        <h4 style="font-size: 0.95rem; font-weight: 600;">
                                            <a href="${pageContext.request.contextPath}/products/${item.productId}">
                                                <c:out value="${item.productName}" />
                                            </a>
                                        </h4>
                                        <div style="font-size: 0.8rem; color: var(--text-muted);">
                                            Qty: ${item.quantity} &bull; ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" /> each
                                        </div>
                                    </div>
                                </div>

                                <div>
                                    <c:if test="${order.status eq 'DELIVERED'}">
                                        <a href="${pageContext.request.contextPath}/products/${item.productId}#reviews" 
                                           class="btn btn-secondary btn-sm" style="border-color: #f59e0b; color: #b45309;">
                                            ★ Write Verified Review
                                        </a>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>

        <c:if test="${empty orders}">
            <div style="text-align: center; padding: 4rem 1rem; background: #fff; border: 1px solid var(--border-color); border-radius: 8px;">
                <p style="font-size: 1.5rem; margin-bottom: 0.5rem;">📦</p>
                <h3 style="font-size: 1.2rem; font-weight: 600;">No Orders Placed Yet</h3>
                <p style="color: var(--text-muted); margin-top: 0.5rem; margin-bottom: 1.5rem;">Your completed purchases will appear here.</p>
                <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Start Shopping</a>
            </div>
        </c:if>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
