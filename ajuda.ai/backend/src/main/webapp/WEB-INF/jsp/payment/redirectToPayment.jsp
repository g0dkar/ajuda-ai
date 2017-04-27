<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><fmt:message key="redirectToPayment.title" /></title>
</head>
<body>
<form id="form_redirect" action="${action}" method="post" accept-charset="<c:out value="${charset}" default="UTF-8" />">
<p><fmt:message key="redirectToPayment.message" /></p>
<c:forEach items="${paramMap}" var="p"><input type="hidden" name="<c:out value="${p.key}" />" value="<c:out value="${p.value}" />"></c:forEach>
<button type="submit"><fmt:message key="redirectToPayment.submit" /></button>
</form>
<script>document.getElementById("form_redirect").submit()</script>
</body>
</html>
