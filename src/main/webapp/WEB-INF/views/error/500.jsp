<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="500 - Server Error — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content" style="text-align: center; padding: 4rem 1rem;">
    <h1 style="font-size: 3rem; color: var(--danger); margin-bottom: 0.5rem;">500</h1>
    <h2 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem;">Unexpected System Error</h2>
    <p style="color: var(--text-muted); max-width: 500px; margin: 0 auto 1.5rem;">
        An unexpected error occurred while processing your request. Our technical lead has been notified. 
        Please try again shortly.
    </p>
    <div style="display: flex; gap: 0.75rem; justify-content: center;">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-secondary">Go to Homepage</a>
        <a href="javascript:location.reload();" class="btn btn-primary">Retry Action</a>
    </div>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
