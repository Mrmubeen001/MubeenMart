<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="400 - Bad Request — MubeenMart" />
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/common/nav.jsp" %>

<main class="main-content" style="text-align: center; padding: 4rem 1rem;">
    <h1 style="font-size: 3rem; color: var(--warning); margin-bottom: 0.5rem;">400</h1>
    <h2 style="font-size: 1.5rem; font-weight: 600; margin-bottom: 1rem;">Invalid Request Parameters</h2>
    <p style="color: var(--text-muted); max-width: 500px; margin: 0 auto 1.5rem;">
        The request submitted to MubeenMart could not be processed due to invalid or malformed data.
    </p>
    <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">Return to Marketplace Home</a>
</main>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>
