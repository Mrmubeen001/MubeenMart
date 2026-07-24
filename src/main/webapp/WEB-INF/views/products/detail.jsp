<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="${product.name} — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <div style="margin-bottom: 1rem;">
        <a href="${pageContext.request.contextPath}/home" style="font-size: 0.875rem; color: var(--text-muted);">
            &larr; Back to Marketplace Catalog
        </a>
    </div>

    <c:if test="${not empty param.reviewAdded}">
        <div class="alert alert-success">Thank you! Your verified purchase review has been submitted successfully.</div>
    </c:if>
    <c:if test="${not empty param.reviewError}">
        <div class="alert alert-danger"><c:out value="${param.reviewError}" /></div>
    </c:if>

    <!-- Product Overview Card -->
    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 2.5rem; background: #fff; padding: 2rem; border-radius: 12px; border: 1px solid var(--border-color); box-shadow: var(--shadow-sm); margin-bottom: 2rem;">
        <div>
            <img src="${product.imageUrl}" alt="<c:out value='${product.name}' />" 
                 style="width: 100%; max-height: 400px; object-fit: cover; border-radius: 8px; border: 1px solid var(--border-color);"
                 onerror="this.src='https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80'">
        </div>

        <div style="display: flex; flex-direction: column;">
            <span style="font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.5px; color: var(--text-muted); font-weight: 600; margin-bottom: 0.5rem;">
                <c:out value="${product.category}" />
            </span>
            <h1 style="font-size: 1.75rem; font-weight: 700; color: var(--secondary); margin-bottom: 0.75rem; line-height: 1.25;">
                <c:out value="${product.name}" />
            </h1>

            <div style="display: flex; align-items: center; gap: 0.5rem; margin-bottom: 1rem;">
                <span style="color: var(--accent); font-size: 1.1rem;">★</span>
                <strong><fmt:formatNumber value="${product.averageRating}" maxFractionDigits="1" minFractionDigits="1" /></strong>
                <span style="color: var(--text-muted); font-size: 0.875rem;">(${product.reviewCount} customer reviews)</span>
                <span style="color: var(--border-color);">&bull;</span>
                <span style="font-size: 0.875rem; color: var(--text-muted);">Sold by: <strong><c:out value="${product.sellerName}" /></strong></span>
            </div>

            <div style="font-size: 2rem; font-weight: 800; color: var(--secondary); margin-bottom: 1rem;">
                ₹<fmt:formatNumber value="${product.price}" pattern="#,##0.00" />
            </div>

            <p style="color: #475569; font-size: 0.95rem; line-height: 1.6; margin-bottom: 1.5rem;">
                <c:out value="${product.description}" />
            </p>

            <div style="margin-top: auto; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                <div style="margin-bottom: 1rem;">
                    <c:choose>
                        <c:when test="${product.stockQty > 0}">
                            <span class="badge" style="background: #dcfce7; color: #166534; font-size: 0.85rem; padding: 4px 10px;">
                                ✓ In Stock (${product.stockQty} units ready to ship)
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-cancelled" style="font-size: 0.85rem; padding: 4px 10px;">
                                Currently Out of Stock
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:if test="${product.stockQty > 0}">
                    <form action="${pageContext.request.contextPath}/cart" method="post" style="display: flex; gap: 0.75rem; align-items: center; flex-wrap: wrap;">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.id}">
                        <div style="display: flex; align-items: center; border: 1px solid var(--border-color); border-radius: 6px; overflow: hidden;">
                            <label for="qtyInput" style="font-size: 0.8rem; padding: 0.5rem 0.75rem; background: #f8fafc; border-right: 1px solid var(--border-color); color: var(--text-muted);">Qty</label>
                            <input type="number" id="qtyInput" name="quantity" value="1" min="1" max="${product.stockQty}" 
                                   style="width: 60px; border: none; padding: 0.5rem; text-align: center; font-size: 0.9rem; outline: none;">
                        </div>
                        <button type="submit" class="btn btn-primary" style="padding: 0.65rem 1.5rem; font-size: 0.95rem;">
                            🛒 Add to Shopping Cart
                        </button>
                    </form>
                </c:if>

                <div style="margin-top: 1rem; display: flex; gap: 0.5rem;">
                    <form action="${pageContext.request.contextPath}/wishlist/add" method="post">
                        <input type="hidden" name="productId" value="${product.id}">
                        <button type="submit" class="btn btn-secondary btn-sm">
                            <c:choose>
                                <c:when test="${inWishlist}">❤️ Saved in Wishlist</c:when>
                                <c:otherwise>🤍 Save to Wishlist</c:otherwise>
                            </c:choose>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Product Reviews & Ratings (Requirement F8) -->
    <section style="background: #fff; padding: 2rem; border-radius: 12px; border: 1px solid var(--border-color); box-shadow: var(--shadow-sm);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; flex-wrap: wrap; gap: 1rem;">
            <div>
                <h2 style="font-size: 1.4rem; font-weight: 700;">Customer Reviews & Ratings</h2>
                <p style="color: var(--text-muted); font-size: 0.875rem;">Verified buyer feedback for completed orders</p>
            </div>
            <div style="display: flex; align-items: center; gap: 0.75rem; background: #f8fafc; padding: 0.5rem 1rem; border-radius: 8px; border: 1px solid var(--border-color);">
                <span style="font-size: 1.75rem; font-weight: 800; color: var(--secondary);">
                    <fmt:formatNumber value="${product.averageRating}" maxFractionDigits="1" minFractionDigits="1" />
                </span>
                <div>
                    <div style="color: var(--accent); font-size: 1rem;">★★★★★</div>
                    <span style="font-size: 0.75rem; color: var(--text-muted);">${reviews.size()} total ratings</span>
                </div>
            </div>
        </div>

        <!-- Review Submission Form (Only enabled for completed buyers per F8) -->
        <c:choose>
            <c:when test="${eligibleToReview}">
                <div style="background: #f0fdf4; border: 1px solid #bbf7d0; padding: 1.25rem; border-radius: 8px; margin-bottom: 2rem;">
                    <h3 style="font-size: 1rem; font-weight: 600; color: #166534; margin-bottom: 0.5rem;">
                        ✍️ Leave a Verified Purchase Review
                    </h3>
                    <form action="${pageContext.request.contextPath}/reviews/submit" method="post">
                        <input type="hidden" name="productId" value="${product.id}">
                        <div class="form-group" style="margin-bottom: 0.75rem;">
                            <label class="form-label" style="color: #166534;">Your Rating:</label>
                            <select name="rating" class="form-control" style="max-width: 200px;" required>
                                <option value="5">★★★★★ (5 Stars - Excellent)</option>
                                <option value="4">★★★★☆ (4 Stars - Very Good)</option>
                                <option value="3">★★★☆☆ (3 Stars - Average)</option>
                                <option value="2">★★☆☆☆ (2 Stars - Below Average)</option>
                                <option value="1">★☆☆☆☆ (1 Star - Poor)</option>
                            </select>
                        </div>
                        <div class="form-group" style="margin-bottom: 0.75rem;">
                            <label class="form-label" style="color: #166534;">Your Comments & Feedback:</label>
                            <textarea name="comment" class="form-control" rows="3" placeholder="Share your experience with build quality, battery, performance..." required></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary btn-sm">Submit Review</button>
                    </form>
                </div>
            </c:when>
            <c:otherwise>
                <div style="background: #f8fafc; border: 1px dashed var(--border-color); padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem; font-size: 0.85rem; color: var(--text-muted);">
                    ℹ️ <em>Verified Review Policy (Requirement F8): Star ratings and reviews are open to buyers who have purchased and received this product in a completed delivery.</em>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Reviews List -->
        <div style="display: flex; flex-direction: column; gap: 1rem;">
            <c:forEach var="rev" items="${reviews}">
                <div style="border-bottom: 1px solid var(--border-color); padding-bottom: 1rem;">
                    <div style="display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 0.25rem;">
                        <span style="font-weight: 600; font-size: 0.95rem;">
                            <c:out value="${rev.userName}" /> 
                            <span class="badge badge-delivered" style="font-size: 0.7rem; margin-left: 0.5rem;">Verified Purchase</span>
                        </span>
                        <span style="font-size: 0.75rem; color: var(--text-muted);">${rev.createdAt}</span>
                    </div>
                    <div style="color: var(--accent); font-size: 0.9rem; margin-bottom: 0.4rem;">
                        <c:forEach begin="1" end="${rev.rating}">★</c:forEach>
                        <c:forEach begin="${rev.rating + 1}" end="5">☆</c:forEach>
                    </div>
                    <p style="color: #334155; font-size: 0.9rem; line-height: 1.5;">
                        <c:out value="${rev.comment}" />
                    </p>
                </div>
            </c:forEach>

            <c:if test="${empty reviews}">
                <p style="color: var(--text-muted); font-size: 0.9rem; padding: 1rem 0;">No reviews written yet. Be the first verified buyer to leave feedback!</p>
            </c:if>
        </div>
    </section>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
