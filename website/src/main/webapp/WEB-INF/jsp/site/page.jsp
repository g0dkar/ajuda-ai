<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ajudaai:page>
	<div class="slider">
		<div><div class="slider-item">
			<div class="slider-img">
				<img src="<c:out value="${page.headerImage}" default="${cdn}/res/img/default-page-banner.jpg" />" alt="Imagem decorativa desta página">
			</div>
			<div class="slider-title">
				<h1>${page.headerLine1}</h1>
				<c:if test="${page.headerLine2 ne null}"><h3>${page.headerLine2}</h3></c:if>
			</div>
		</div></div>
	</div>
	
	<div class="container content-page">
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<h1 class="page-title"><c:out value="${page.title}" /></h1>
						<c:if test="${page.subtitle ne null}"><h4 class="page-subtitle"><c:out value="${page.subtitle}" /></h4></c:if>
						
						<hr>
						
						${page.contentMarkdown}
						
						<hr>
						
						<div class="page-signature"><small>Publicado em <time datetime="<fmt:formatDate value="${page.creation.time}" pattern="yyyy-MM-dd'T'HH:mm'Z'" timeZone="UTC" />"><fmt:formatDate value="${page.creation.time}" pattern="dd/MM/yyyy HH'h'mm'm'" /></time><c:if test="${page.creation.lastUpdate ne null}">, Última alteração: <time datetime="<fmt:formatDate value="${page.creation.lastUpdate}" pattern="yyyy-MM-dd'T'HH:mm'Z'" timeZone="UTC" />"><fmt:formatDate value="${page.creation.lastUpdate}" pattern="dd/MM/yyyy HH'h'mm'm'" /></time></c:if></small></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>