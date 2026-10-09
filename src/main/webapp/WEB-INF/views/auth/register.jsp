<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Create Account — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <div class="form-card">
        <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 0.5rem; text-align: center;">
            Join MubeenMart
        </h2>
        <p style="color: var(--text-muted); font-size: 0.875rem; text-align: center; margin-bottom: 1.5rem;">
            Create your marketplace account to browse or sell
        </p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-group">
                <label for="name" class="form-label">Full Name</label>
                <input type="text" id="name" name="name" class="form-control" 
                       value="<c:out value='${name}' />" required placeholder="e.g. Mohamed Mubeen">
            </div>

            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="<c:out value='${email}' />" required placeholder="name@example.com">
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password (min 6 characters)</label>
                <input type="password" id="password" name="password" class="form-control" minlength="6" required placeholder="••••••••">
            </div>

            <div class="form-group">
                <label class="form-label">Account Role</label>
                <div style="display: flex; gap: 1.5rem; margin-top: 0.35rem;">
                    <label style="display: flex; align-items: center; gap: 0.35rem; font-size: 0.9rem; cursor: pointer;">
                        <input type="radio" name="role" value="BUYER" ${empty role || role eq 'BUYER' ? 'checked' : ''}>
                        <span>Buyer (Shop & Purchase)</span>
                    </label>
                    <label style="display: flex; align-items: center; gap: 0.35rem; font-size: 0.9rem; cursor: pointer;">
                        <input type="radio" name="role" value="SELLER" ${role eq 'SELLER' ? 'checked' : ''}>
                        <span>Seller (List & Manage Items)</span>
                    </label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="padding: 0.65rem; font-size: 0.95rem; margin-top: 1.5rem;">
                Create Account
            </button>
        </form>

        <p style="text-align: center; margin-top: 1.25rem; font-size: 0.875rem; color: var(--text-muted);">
            Already have an account? <a href="${pageContext.request.contextPath}/login">Sign In</a>
        </p>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
