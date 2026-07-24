<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Seller Portal Dashboard — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
        <div>
            <h1 style="font-size: 1.75rem; font-weight: 700;">Seller Business Dashboard</h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">
                Manage your store listings, track incoming customer orders, and view sales metrics
            </p>
        </div>
        <button type="button" class="btn btn-primary" onclick="document.getElementById('createProductModal').style.display='block'">
            + Add New Product Listing
        </button>
    </div>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
    </c:if>
    <c:if test="${param.productCreated eq 'true'}">
        <div class="alert alert-success">Product listing created successfully!</div>
    </c:if>
    <c:if test="${param.productUpdated eq 'true'}">
        <div class="alert alert-success">Product listing updated successfully!</div>
    </c:if>
    <c:if test="${param.productDeleted eq 'true'}">
        <div class="alert alert-success">Product listing removed.</div>
    </c:if>
    <c:if test="${param.orderUpdated eq 'true'}">
        <div class="alert alert-success">Order fulfillment status updated successfully.</div>
    </c:if>

    <!-- O3: Seller Sales Dashboard Metrics -->
    <div class="dashboard-grid">
        <div class="metric-card">
            <div class="metric-title">TOTAL REVENUE EARNED</div>
            <div class="metric-value" style="color: var(--primary);">
                ₹<fmt:formatNumber value="${summary.totalRevenue}" pattern="#,##0.00" />
            </div>
        </div>
        <div class="metric-card">
            <div class="metric-title">TOTAL ORDERS RECEIVED</div>
            <div class="metric-value">${summary.totalOrders}</div>
        </div>
        <div class="metric-card">
            <div class="metric-title">UNITS DISPATCHED / SOLD</div>
            <div class="metric-value">${summary.totalItemsSold}</div>
        </div>
        <div class="metric-card">
            <div class="metric-title">ACTIVE CATALOG LISTINGS</div>
            <div class="metric-value">${summary.activeListings}</div>
        </div>
    </div>

    <!-- F2: Manage Product Listings -->
    <section style="background: #fff; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm); margin-bottom: 2rem;">
        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
            Your Active Product Listings (${products.size()})
        </h2>

        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock Qty</th>
                        <th>Rating</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${products}">
                        <tr>
                            <td>
                                <div style="display: flex; align-items: center; gap: 0.75rem;">
                                    <img src="${p.imageUrl}" alt="<c:out value='${p.name}' />" 
                                         style="width: 44px; height: 44px; object-fit: cover; border-radius: 4px; border: 1px solid var(--border-color);"
                                         onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
                                    <div>
                                        <a href="${pageContext.request.contextPath}/products/${p.id}" target="_blank" style="font-weight: 600;">
                                            <c:out value="${p.name}" />
                                        </a>
                                        <div style="font-size: 0.75rem; color: var(--text-muted); max-width: 280px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                            <c:out value="${p.description}" />
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td><span class="badge" style="background: #f1f5f9; color: #475569;"><c:out value="${p.category}" /></span></td>
                            <td style="font-weight: 600;">₹<fmt:formatNumber value="${p.price}" pattern="#,##0.00" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.stockQty > 5}">
                                        <span style="color: var(--success); font-weight: 600;">${p.stockQty}</span>
                                    </c:when>
                                    <c:when test="${p.stockQty > 0}">
                                        <span style="color: var(--warning); font-weight: 600;">Low (${p.stockQty})</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: var(--danger); font-weight: 600;">0 (Out of stock)</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>★ <fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1" minFractionDigits="1" /> (${p.reviewCount})</td>
                            <td>
                                <div style="display: flex; gap: 0.35rem;">
                                    <button type="button" class="btn btn-secondary btn-sm"
                                            onclick="openEditModal('${p.id}', '<c:out value="${p.name}" />', '<c:out value="${p.description}" />', '${p.price}', '${p.stockQty}', '<c:out value="${p.category}" />', '<c:out value="${p.imageUrl}" />')">
                                        Edit
                                    </button>
                                    <form action="${pageContext.request.contextPath}/seller/products/delete" method="post" 
                                          onsubmit="return confirm('Delete listing: <c:out value="${p.name}" />?');" style="margin: 0;">
                                        <input type="hidden" name="productId" value="${p.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty products}">
                        <tr>
                            <td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                You haven't listed any products yet. Click "+ Add New Product Listing" to start selling.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </section>

    <!-- F6 & O2: Incoming Orders for Seller -->
    <section style="background: #fff; padding: 1.5rem; border-radius: var(--radius); border: 1px solid var(--border-color); box-shadow: var(--shadow-sm);">
        <h2 style="font-size: 1.25rem; font-weight: 700; margin-bottom: 1rem; border-bottom: 1px solid var(--border-color); padding-bottom: 0.5rem;">
            Incoming Customer Orders (${incomingOrders.size()})
        </h2>

        <div class="table-responsive">
            <table class="table">
                <thead>
                    <tr>
                        <th>Order #</th>
                        <th>Buyer</th>
                        <th>Items Ordered</th>
                        <th>Placed On</th>
                        <th>Current Status</th>
                        <th>Update Status (Workflow O2)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${incomingOrders}">
                        <tr>
                            <td style="font-weight: 600;">#${order.id}</td>
                            <td>
                                <c:out value="${order.buyerName}" />
                                <div style="font-size: 0.75rem; color: var(--text-muted);"><c:out value="${order.buyerEmail}" /></div>
                            </td>
                            <td>
                                <c:forEach var="item" items="${order.items}">
                                    <div style="font-size: 0.85rem;">
                                        &bull; <c:out value="${item.productName}" /> (Qty: ${item.quantity} &times; ₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00" />)
                                    </div>
                                </c:forEach>
                            </td>
                            <td style="font-size: 0.8rem; color: var(--text-muted);">${order.createdAt}</td>
                            <td>
                                <span class="badge badge-${order.status.name().toLowerCase()}">
                                    <c:out value="${order.status}" />
                                </span>
                            </td>
                            <td>
                                <form action="${pageContext.request.contextPath}/seller/orders/status" method="post" style="display: flex; gap: 0.35rem; align-items: center;">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <select name="status" class="form-control" style="padding: 0.3rem 0.5rem; font-size: 0.8rem; max-width: 140px;">
                                        <option value="PENDING" ${order.status eq 'PENDING' ? 'selected' : ''}>Pending</option>
                                        <option value="CONFIRMED" ${order.status eq 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                        <option value="SHIPPED" ${order.status eq 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                        <option value="DELIVERED" ${order.status eq 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                        <option value="CANCELLED" ${order.status eq 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                    <button type="submit" class="btn btn-primary btn-sm">Update</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty incomingOrders}">
                        <tr>
                            <td colspan="6" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                No orders received for your products yet.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </section>
</main>

<!-- Modal: Create Product -->
<div id="createProductModal" style="display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 1000; overflow-y: auto;">
    <div style="background: #fff; max-width: 550px; margin: 3rem auto; padding: 2rem; border-radius: 12px; box-shadow: var(--shadow-lg);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem;">
            <h3 style="font-size: 1.25rem; font-weight: 700;">Add New Product Listing</h3>
            <button type="button" onclick="document.getElementById('createProductModal').style.display='none'" style="border: none; background: none; font-size: 1.5rem; cursor: pointer;">&times;</button>
        </div>

        <form action="${pageContext.request.contextPath}/seller/products/create" method="post">
            <div class="form-group">
                <label class="form-label">Product Title *</label>
                <input type="text" name="name" class="form-control" required placeholder="e.g. Ergonomic Bluetooth Mouse">
            </div>

            <div class="form-group">
                <label class="form-label">Category *</label>
                <select name="category" class="form-control" required>
                    <option value="Electronics">Electronics</option>
                    <option value="Fashion">Fashion</option>
                    <option value="Home">Home & Living</option>
                    <option value="Books">Books & Academic</option>
                    <option value="Accessories">Accessories</option>
                </select>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label class="form-label">Price (₹) *</label>
                    <input type="number" step="0.01" name="price" class="form-control" min="0.01" required placeholder="499.00">
                </div>
                <div class="form-group">
                    <label class="form-label">Initial Stock Quantity *</label>
                    <input type="number" name="stockQty" class="form-control" min="0" required placeholder="25">
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="url" name="imageUrl" class="form-control" placeholder="https://images.unsplash.com/...">
            </div>

            <div class="form-group">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-control" rows="3" placeholder="Highlight key features and specifications..."></textarea>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;">
                <button type="button" class="btn btn-secondary" onclick="document.getElementById('createProductModal').style.display='none'">Cancel</button>
                <button type="submit" class="btn btn-primary">Publish Listing</button>
            </div>
        </form>
    </div>
</div>

<!-- Modal: Edit Product -->
<div id="editProductModal" style="display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 1000; overflow-y: auto;">
    <div style="background: #fff; max-width: 550px; margin: 3rem auto; padding: 2rem; border-radius: 12px; box-shadow: var(--shadow-lg);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.25rem;">
            <h3 style="font-size: 1.25rem; font-weight: 700;">Edit Product Listing</h3>
            <button type="button" onclick="document.getElementById('editProductModal').style.display='none'" style="border: none; background: none; font-size: 1.5rem; cursor: pointer;">&times;</button>
        </div>

        <form action="${pageContext.request.contextPath}/seller/products/edit" method="post">
            <input type="hidden" id="editProductId" name="productId">

            <div class="form-group">
                <label class="form-label">Product Title *</label>
                <input type="text" id="editName" name="name" class="form-control" required>
            </div>

            <div class="form-group">
                <label class="form-label">Category *</label>
                <select id="editCategory" name="category" class="form-control" required>
                    <option value="Electronics">Electronics</option>
                    <option value="Fashion">Fashion</option>
                    <option value="Home">Home & Living</option>
                    <option value="Books">Books & Academic</option>
                    <option value="Accessories">Accessories</option>
                </select>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label class="form-label">Price (₹) *</label>
                    <input type="number" step="0.01" id="editPrice" name="price" class="form-control" min="0.01" required>
                </div>
                <div class="form-group">
                    <label class="form-label">Stock Quantity *</label>
                    <input type="number" id="editStockQty" name="stockQty" class="form-control" min="0" required>
                </div>
            </div>

            <div class="form-group">
                <label class="form-label">Image URL</label>
                <input type="url" id="editImageUrl" name="imageUrl" class="form-control">
            </div>

            <div class="form-group">
                <label class="form-label">Description</label>
                <textarea id="editDescription" name="description" class="form-control" rows="3"></textarea>
            </div>

            <div style="display: flex; justify-content: flex-end; gap: 0.75rem; margin-top: 1.5rem;">
                <button type="button" class="btn btn-secondary" onclick="document.getElementById('editProductModal').style.display='none'">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Changes</button>
            </div>
        </form>
    </div>
</div>

<script>
function openEditModal(id, name, desc, price, stock, cat, img) {
    document.getElementById('editProductId').value = id;
    document.getElementById('editName').value = name;
    document.getElementById('editDescription').value = desc;
    document.getElementById('editPrice').value = price;
    document.getElementById('editStockQty').value = stock;
    document.getElementById('editCategory').value = cat;
    document.getElementById('editImageUrl').value = img;
    document.getElementById('editProductModal').style.display = 'block';
}
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
