<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:adminPage menu="2" title="Área Administrativa">
	<div class="content-container" ng-controller="DashboardController">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1 class="page-title">Área Administrativa - Instituições</h1>
				</div>
			</div>
			
			<div class="row institution-submenu">
				<div class="col-xs-12">
					<ul class="nav nav-pills">
						<li role="presentation" class="active"><a href="/admin/area" title="Gerenciar Instituições">Instituições</a></li>
						<li role="presentation"><a href="/admin/area/flagged" title="Gerenciar Denúncias">Denúncias</a></li>
					</ul>
				</div>
			</div>
			
			<c:if test="${errors ne null && !empty(errors)}">
				<div class="row alerts-row">
					<div class="col-xs-12">
						<c:forEach items="${errors}" var="err">
							<div class="alert alert-danger" role="alert"><strong>Erro:</strong> ${err.message}</div>
						</c:forEach>
					</div>
				</div>
			</c:if>
			
			<c:if test="${infoMessage ne null}">
				<div class="row alerts-row">
					<div class="col-xs-12">
						<div class="alert alert-info" role="alert"><strong>Informação:</strong> ${infoMessage}</div>
					</div>
				</div>
			</c:if>
			
			<div class="row">
				<div class="col-xs-12">Sendo feito.</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>