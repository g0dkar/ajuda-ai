<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ajudaai:adminPage menu="1" title="${institution.name}">
	<div class="content-container">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1 class="page-title"><c:out value="${institution.name}" /></h1>
				</div>
			</div>
			
			<div class="row institution-submenu">
				<div class="col-xs-12">
					<ul class="nav nav-pills">
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}" title="Página com informações gerais sobre esta Instituição/ONG">Resumo</a></li>
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}/posts" title="Gerencie os Posts desta Instituição/ONG">Posts</a></li>
						<li role="presentation" class="active"><a href="/admin/instituicao/${institution.slug}/transparencia" title="Informe os gastos desta Instituição/ONG">Transparência</a></li>
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}/detalhes" title="Configure alguns detalhes como logo, banner, nome da instituição, etc.">Detalhes</a></li>
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
				<div class="col-xs-12">
					<h2>Transparência</h2>
					
					<div class="graph-placeholder">Ainda estamos fazendo esta parte ;)</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>