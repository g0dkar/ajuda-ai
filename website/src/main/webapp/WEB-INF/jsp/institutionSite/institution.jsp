<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div id="page-institution" ng-controller="InstitutionController">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<div class="hero">
						<!--img src="/res/img/ama-header.jpg" class="hero-img"-->
						<img src="http://4.bp.blogspot.com/-vCjZ1ZNuRJw/VmxGLPHls5I/AAAAAAAABWc/yBSGgUqnsmQ/s1600-r/banner_AMA1.jpg" class="hero-img">
						
						<div class="hero-page-title">
							<div class="hero-page-title-logo">
								<div class="container">
									<div class="row">
										<div class="col-xs-12 col-sm-3 col-md-2">
											<div class="institution-logo">
												<img src="<c:out value="${institution.logo}" default="/res/img/institution-default.jpg"></c:out>" alt="Logo" title="Logo da Instituição/ONG que você pode ajudar">
											</div>
										</div>
										<div class="col-xs-12 col-sm-9 col-md-10">
											<h1 class="hero-title"><strong><c:out value="${institution.name}" /></strong> precisa da sua ajuda!</h1>
										</div>
									</div>
								</div>
							</div>
							
							<div class="hero-page-title-info">
								<div class="container">
									<div class="row">
										<div class="col-xs-12 col-offset-sm-3 col-sm-2" ng-class="{loadingInfo:'loading'}">
											<div class="institution-donors" ng-bind="donations.count | number" title="Quantidade de pessoas que fizeram doações a esta Instituição/ONG"></div>
											<div class="institution-donors-caption"><ng-pluralize count="donations.count" when="{'0': 'nenhum ajudante :(', 'one': 'ajudante', 'other': 'ajudantes'}"></ng-pluralize></div>
										</div>
										<div class="col-xs-12 col-sm-2" ng-class="{loadingInfo:'loading'}">
											<div class="institution-value" ng-bind="donations.value | currency" title="Valor arrecadado para esta Instituição/ONG"></div>
											<div class="institution-value-caption">arrecadados</div>
										</div>
										<div class="col-xs-12 col-offset-sm-2 col-sm-3">
											<div class="donate-button" ng-click="donate()">Faça uma Doação</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<div class="page-content">
						<h1 class="page-title">Sobre quem você pode ajudar</h1>
						<h4 class="page-subtitle">Conheça um pouco do trabalho que você pode ajudar a continuar</h4>
						
						<hr>
						
						${institutionDescriptionMarkdown}
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>