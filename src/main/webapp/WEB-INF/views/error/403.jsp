<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="403 - Forbidden — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content" style="text-align: center; padding: 4rem 1rem;">
    <h1 style="font-size: 3rem; color: var(--danger); margin-bottom: 0.5rem;">403</h1>
    <h2 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem;">Access Restricted</h2>
    <p style="color: var(--text-muted); max-width: 500px; margin: 0 auto 1.5rem;">
        You do not have the required role or permissions to access this page. Please log in with an authorized account.
    </p>
    <div style="display: flex; gap: 0.75rem; justify-content: center;">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Home</a>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Switch Account</a>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
