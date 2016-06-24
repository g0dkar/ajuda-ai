<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ajudaai:adminPage menu="1" title="${institution.name}">
	<script type="text/javascript">var $institution = ${institution.toJson()};</script>
	<div class="content-container" ng-controller="InstitutionDetailsController">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1 class="page-title" ng-bind="institution.name"><c:out value="${institution.name}" /></h1>
				</div>
			</div>
			
			<div class="row institution-submenu">
				<div class="col-xs-12">
					<ul class="nav nav-pills">
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}" title="Página com informações gerais sobre esta Instituição/ONG">Resumo</a></li>
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}/posts" title="Gerencie os Posts desta Instituição/ONG">Posts</a></li>
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}/transparencia" title="Informe os gastos desta Instituição/ONG">Transparência</a></li>
						<li role="presentation" class="active"><a href="/admin/instituicao/${institution.slug}/detalhes" title="Configure alguns detalhes como logo, banner, nome da instituição, etc.">Detalhes</a></li>
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
					<h2>Detalhes</h2>
					<p>Configure altumas características da sua instituição. Algumas coisas ainda estão sendo desenvolvidas então tente sempre voltar aqui dar uma olhadinha se há algo novo :)</p>
					
					<form action="/admin/instituicao/${institution.slug}/detalhes" method="post" class="form-horizontal">
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">Nome de sua Inst./ONG *</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="name" name="name" ng-model="institution.name" placeholder="Nome de sua Instituição/ONG" value="<c:out value="${institution.name}" />" required autofocus>
								<p class="help-block">Nome que utilizaremos para chamar sua Instituição/ONG no Website :)</p>
							</div>
						</div>
						
						<div class="form-group" ng-class="{'has-warning has-feedback': unavailableSlug}">
							<label for="name" class="col-sm-2 control-label">Endereço *</label>
							<div class="col-sm-10">
								<div class="input-group">
									<span class="input-group-addon">https://ajuda.ai/</span>
									<input type="text" class="form-control" id="slug" name="slug" placeholder="Endereço que você quer no Ajuda.Ai" value="<c:out value="${institution.slug}" />" aria-describedby="slugStatus">
								</div>
								<span class="glyphicon glyphicon-warning-sign form-control-feedback" aria-hidden="true" ng-show="unavailableSlug"></span>
								<p id="slugStatus" class="help-block" ng-bind="unavailableSlug ? 'Endereço Indisponível' : 'Endereço Disponível'">Endereço Disponível</p>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>