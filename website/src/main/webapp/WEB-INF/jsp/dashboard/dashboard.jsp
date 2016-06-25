<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:adminPage menu="0" title="Resumo">
	<div class="content-container" ng-controller="DashboardController">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1 class="page-title">Resumo deste Mês</h1>
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
			
			<div class="row dashboard-data-panels" ng-class="{'updating':updating}">
				<div class="col-xs-6 col-sm-3 dashboard-data">
					<h3>Ajudantes</h3>
					<div class="dashboard-number" ng-bind="(data.helpers || 0) | number">0</div>
				</div>
				<div class="col-xs-6 col-sm-3 dashboard-data">
					<h3>Pago</h3>
					<div class="dashboard-number dashboard-currency" ng-bind="(data.currentData.paidDonations || 0) | currency">$ 0,00</div>
				</div>
				<div class="col-xs-6 col-sm-3 dashboard-data">
					<h3>Disponível</h3>
					<div class="dashboard-number dashboard-currency" ng-bind="(data.currentData.availableDonations || 0) | currency">$ 0,00</div>
				</div>
				<div class="col-xs-6 col-sm-3 dashboard-data">
					<h3>Média</h3>
					<div class="dashboard-number dashboard-currency" ng-bind="(data.currentData.meanDonation || 0) | currency">$ 0,00</div>
				</div>
			</div>
			
			<div class="row dashboard-graph" ng-class="{'updating':updating}">
				<div class="col-xs-12">
					<h3>Doações neste Mês</h3>
					<div class="graph-placeholder">Aguarde, este Gráfico está sendo implementado :)</div>
				</div>
			</div>
			
			<div class="row dashboard-institutions" ng-class="{'updating':updating}">
				<div class="col-xs-12">
					<h3>Suas Instituições/ONGs <small><strong ng-bind="data.institutions.length | number"></strong> Instituições/ONGs</small></h3>
					
					<ul class="nav nav-pills nav-stacked dashboard-institutions">
						<li ng-repeat="institution in data.institutions"><a href="/admin/instituicao/{{::institution.slug}}"><span class="institution-name" ng-bind="::institution.name"></span><span class="institution-slug" ng-bind="::('/' + institution.slug)"></span><span class="institution-extra">Criada em {{::institution.creation.time | date}}, Usando {{::institution.paymentService}}</span></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>