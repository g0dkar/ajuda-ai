<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<title>Redirecionando...</title>
</head>
<body>
	<form action="${action}" method="post" id="redirectPost"<c:if test="${charset ne null}"> accept-charset="${charset}"</c:if>>
		<c:forEach items="${paramMap.entrySet()}" var="p">
			<input type="hidden" name="${p.key}" value="<c:out value="${p.value}" />">
		</c:forEach>
		<button type="submit">Caso o redirecionamento não aconteça, clique aqui</button>
	</form>
	<script type="text/javascript">document.getElementById("redirectPost").submit()</script>
</body>
</html>