<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Order #${order.id} Details — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <div style="margin-bottom: 1rem;">
        <a href="${pageContext.request.contextPath}/orders" style="font-size: 0.875rem; color: var(--text-muted);">
            &larr; Back to Order History
        </a>
    </div>

    <div style="background: #fff; border: 1px solid var(--border-color); border-radius: var(--radius); padding: 2rem; box-shadow: var(--shadow-sm);">
        <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid var(--border-color); padding-bottom: 1rem; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h1 style="font-size: 1.5rem; font-weight: 700;">Order #${order.id}</h1>
                <p style="color: var(--text-muted); font-size: 0.85rem;">Placed on ${order.createdAt} &bull; Buyer: <c:out value="${order.buyerName}" /></p>
            </div>
            <div>
                <span class="badge badge-${order.status.name().toLowerCase()}" style="font-size: 0.9rem; padding: 6px 14px;">
                    <c:out value="${order.status}" />
                </span>
            </div>
        </div>

        <!-- Workflow Status Tracker -->
        <div style="display: flex; justify-content: space-around; margin: 2rem 0; padding: 1rem; background: #f8fafc; border-radius: 8px; border: 1px solid var(--border-color); text-align: center;">
            <div style="flex: 1; opacity: ${order.status eq 'PENDING' || order.status eq 'CONFIRMED' || order.status eq 'SHIPPED' || order.status eq 'DELIVERED' ? '1' : '0.4'};">
                <div style="font-size: 1.5rem;">📝</div>
                <strong style="font-size: 0.85rem; display: block;">Pending</strong>
            </div>
            <div style="color: var(--border-color); font-size: 1.5rem;">&rarr;</div>
            <div style="flex: 1; opacity: ${order.status eq 'CONFIRMED' || order.status eq 'SHIPPED' || order.status eq 'DELIVERED' ? '1' : '0.4'};">
                <div style="font-size: 1.5rem;">✅</div>
                <strong style="font-size: 0.85rem; display: block;">Confirmed</strong>
            </div>
            <div style="color: var(--border-color); font-size: 1.5rem;">&rarr;</div>
            <div style="flex: 1; opacity: ${order.status eq 'SHIPPED' || order.status eq 'DELIVERED' ? '1' : '0.4'};">
                <div style="font-size: 1.5rem;">🚚</div>
                <strong style="font-size: 0.85rem; display: block;">Shipped</strong>
            </div>
            <div style="color: var(--border-color); font-size: 1.5rem;">&rarr;</div>
            <div style="flex: 1; opacity: ${order.status eq 'DELIVERED' ? '1' : '0.4'};">
                <div style="font-size: 1.5rem;">📦</div>
                <strong style="font-size: 0.85rem; display: block;">Delivered</strong>
            </div>
        </div>

        <h3 style="font-size: 1.15rem; font-weight: 600; margin-bottom: 1rem;">Purchased Items</h3>
        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Price</th>
                        <th>Qty</th>
                        <th>Subtotal</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/products/${item.productId}" style="font-weight: 600;">
                                    <c:out value="${item.productName}" />
                                </a>
                            </td>
                            <td>₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" /></td>
                            <td>${item.quantity}</td>
                            <td style="font-weight: 700;">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00" /></td>
                            <td>
                                <c:if test="${order.status eq 'DELIVERED'}">
                                    <a href="${pageContext.request.contextPath}/products/${item.productId}" class="btn btn-secondary btn-sm">
                                        Review Item
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div style="text-align: right; margin-top: 1.5rem; padding-top: 1rem; border-top: 2px dashed var(--border-color); font-size: 1.25rem;">
            <strong>Total Paid:</strong>
            <strong style="color: var(--primary);">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00" /></strong>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
