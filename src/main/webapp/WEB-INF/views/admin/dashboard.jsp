<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Administrator Control Panel — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <div style="margin-bottom: 1.5rem;">
        <h1 style="font-size: 1.75rem; font-weight: 700; color: #581c87;">🛡️ Marketplace Administration Portal</h1>
        <p style="color: var(--text-muted); font-size: 0.9rem;">
            Central management for users, all system transactions, and catalog moderation
        </p>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${param.productDeleted eq 'true'}">
        <div class="alert alert-success">Product listing removed by administrator moderation.</div>
    </c:if>
    <c:if test="${param.orderUpdated eq 'true'}">
        <div class="alert alert-success">Order status updated.</div>
    </c:if>

    <!-- Admin Platform Metrics -->
    <div class="dashboard-grid">
        <div class="metric-card">
            <div class="metric-title">TOTAL PLATFORM GMV</div>
            <div class="metric-value" style="color: #7c3aed;">
                ₹<fmt:formatNumber value="${totalMarketplaceVolume}" pattern="#,##0.00" />
            </div>
        </div>
        <div class="metric-card">
            <div class="metric-title">REGISTERED USERS</div>
            <div class="metric-value">${users.size()}</div>
        </div>
        <div class="metric-card">
            <div class="metric-title">ALL MARKETPLACE ORDERS</div>
            <div class="metric-value">${orders.size()}</div>
        </div>
        <div class="metric-card">
            <div class="metric-title">PUBLISHED LISTINGS</div>
            <div class="metric-value">${products.size()}</div>
        </div>
    </div>

    <!-- Section 1: User Management (F7) -->
    <section style="background: #fff; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm); margin-bottom: 2rem;">
        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
            Registered User Accounts (${users.size()})
        </h2>

        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Full Name</th>
                        <th>Email Address</th>
                        <th>Assigned Role</th>
                        <th>Registration Date</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${users}">
                        <tr>
                            <td style="font-weight: 600;">#${u.id}</td>
                            <td><c:out value="${u.name}" /></td>
                            <td><c:out value="${u.email}" /></td>
                            <td>
                                <span class="badge" style="background: ${u.role eq 'ADMIN' ? '#f3e8ff' : (u.role eq 'SELLER' ? '#dbeafe' : '#f1f5f9')}; 
                                                           color: ${u.role eq 'ADMIN' ? '#6b21a8' : (u.role eq 'SELLER' ? '#1e40af' : '#475569')};">
                                    <c:out value="${u.role}" />
                                </span>
                            </td>
                            <td style="font-size: 0.8rem; color: var(--text-muted);">${u.createdAt}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <!-- Section 2: Catalog Moderation (F7) -->
    <section style="background: #fff; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm); margin-bottom: 2rem;">
        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
            Listing Moderation & Catalog Oversight (${products.size()})
        </h2>

        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Seller</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Moderation Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td>
                                <div style="display: flex; align-items: center; gap: 0.75rem;">
                                    <img src="${p.imageUrl}" alt="<c:out value='${p.name}' />" 
                                         style="width: 40px; height: 40px; object-fit: cover; border-radius: 4px; border: 1px solid var(--border-color);"
                                         onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
                                    <div>
                                        <a href="${pageContext.request.contextPath}/products/${p.id}" target="_blank" style="font-weight: 600;">
                                            <c:out value="${p.name}" />
                                        </a>
                                        <div style="font-size: 0.75rem; color: var(--text-muted);">ID: #${p.id}</div>
                                    </div>
                                </div>
                            </td>
                            <td><c:out value="${p.sellerName}" /></td>
                            <td><span class="badge" style="background: #f1f5f9; color: #475569;"><c:out value="${p.category}" /></span></td>
                            <td style="font-weight: 600;">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></td>
                            <td>${p.stockQty}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/products/delete" method="post" 
                                      onsubmit="return confirm('Moderate and remove listing: <c:out value="${p.name}" />?');" style="margin: 0;">
                                    <input type="hidden" name="productId" value="${p.id}">
                                    <button type="submit" class="btn btn-danger btn-sm">Remove Listing</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <!-- Section 3: All Marketplace Orders (F7) -->
    <section style="background: #fff; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm);">
        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
            Marketplace Transaction Log (${orders.size()})
        </h2>

        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Order #</th>
                        <th>Buyer</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Created At</th>
                        <th>Admin Override</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="o" items="${orders}">
                        <tr>
                            <td style="font-weight: 600;">#${o.id}</td>
                            <td>
                                <c:out value="${o.buyerName}" />
                                <div style="font-size: 0.75rem; color: var(--text-muted);"><c:out value="${o.buyerEmail}" /></div>
                            </td>
                            <td style="font-weight: 700;">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00" /></td>
                            <td>
                                <span class="badge badge-${o.status.name().toLowerCase()}">
                                    <c:out value="${o.status}" />
                                </span>
                            </td>
                            <td style="font-size: 0.8rem; color: var(--text-muted);">${o.createdAt}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/orders/status" method="post" style="display: flex; gap: 0.35rem; align-items: center;">
                                    <input type="hidden" name="orderId" value="${o.id}">
                                    <select name="status" class="form-control" style="padding: 0.3rem 0.5rem; font-size: 0.8rem; max-width: 130px;">
                                        <option value="PENDING" ${o.status eq 'PENDING' ? 'selected' : ''}>Pending</option>
                                        <option value="CONFIRMED" ${o.status eq 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                        <option value="SHIPPED" ${o.status eq 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                        <option value="DELIVERED" ${o.status eq 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                        <option value="CANCELLED" ${o.status eq 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                    <button type="submit" class="btn btn-secondary btn-sm">Set</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
