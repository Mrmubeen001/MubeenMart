<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Log In — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content">
    <div class="form-card">
        <h2 style="font-size: 1.5rem; font-weight: 700; margin-bottom: 0.5rem; text-align: center;">
            Welcome Back
        </h2>
        <p style="color: var(--text-muted); font-size: 0.875rem; text-align: center; margin-bottom: 1.5rem;">
            Sign in to access your MubeenMart account
        </p>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}" /></div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success"><c:out value="${successMessage}" /></div>
        </c:if>
        <c:if test="${param.loggedOut eq 'true'}">
            <div class="alert alert-success">You have been logged out securely.</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="redirect" value="<c:out value='${redirectUrl}' />">

            <div class="form-group">
                <label for="email" class="form-label">Email Address</label>
                <input type="email" id="email" name="email" class="form-control" 
                       value="<c:out value='${email}' />" required placeholder="name@example.com">
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" required placeholder="••••••••">
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="padding: 0.65rem; font-size: 0.95rem;">
                Sign In
            </button>
        </form>

        <p style="text-align: center; margin-top: 1.25rem; font-size: 0.875rem; color: var(--text-muted);">
            Don't have an account? <a href="${pageContext.request.contextPath}/register">Create an account</a>
        </p>

        <!-- Evaluation Test Credentials Box -->
        <div style="margin-top: 1.5rem; padding: 1rem; background: #f8fafc; border: 1px dashed var(--border-color); border-radius: 6px; font-size: 0.8rem;">
            <p style="font-weight: 600; color: var(--text-main); margin-bottom: 0.35rem;">Demo Evaluation Accounts:</p>
            <ul style="padding-left: 1.2rem; color: var(--text-muted); line-height: 1.6;">
                <li><strong>Admin:</strong> admin@mubeenmart.com (Pass: Password@123)</li>
                <li><strong>Seller:</strong> seller@mubeenmart.com (Pass: Password@123)</li>
                <li><strong>Buyer:</strong> buyer@mubeenmart.com (Pass: Password@123)</li>
            </ul>
        </div>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
