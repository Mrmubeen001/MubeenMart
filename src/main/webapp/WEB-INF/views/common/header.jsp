<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${pageTitle != null ? pageTitle : 'MubeenMart — Online Marketplace'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
    <script>
        window.CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
</head>
<body>
