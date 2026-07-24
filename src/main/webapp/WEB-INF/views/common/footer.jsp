<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/common/chat-widget.jsp" %>

<footer>
    <div style="max-width: 1200px; margin: 0 auto;">
        <p><strong>MubeenMart</strong> — Multi-Seller E-Commerce Marketplace Platform</p>
        <p style="margin-top: 0.25rem;">Developed by Mohamed Mubeen &bull; Anna University R2025 Regulations &bull; Java Servlets &bull; JDBC &bull; Apache Tomcat</p>
        <p style="margin-top: 0.5rem; font-size: 0.75rem; color: #94a3b8;">
            <a href="${pageContext.request.contextPath}/api/v1/health" style="color: #64748b; text-decoration: underline;">System Health Status</a> &bull;
            Mock Payment Enabled &bull; Safe H2 Persistence
        </p>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script src="${pageContext.request.contextPath}/js/chat-widget.js"></script>
</body>
</html>
