<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div id="page-institution" ng-controller="InstitutionController">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<div class="hero">
						<div class="hero-img">
							<img src="http://4.bp.blogspot.com/-vCjZ1ZNuRJw/VmxGLPHls5I/AAAAAAAABWc/yBSGgUqnsmQ/s1600-r/banner_AMA1.jpg">
							
							<div class="hero-img-text">
								<div class="container">
									<div class="row">
										<div class="col-xs-12 col-sm-offset-3 col-md-offset-2 col-sm-11">
											<h1><strong><c:out value="${institution.name}" /></strong> precisa da sua ajuda!</h1>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<div class="hero-extra">
							<div class="container">
								<div class="row">
									<div class="col-xs-12 col-sm-3 col-md-2 text-center">
										<img src="<c:out value="${institution.logo}" default="${pageContext.request.contextPath}/res/img/institution-default.jpg"></c:out>" class="hero-extra-logo" alt="Logo da Instituição/ONG que você pode ajudar">
									</div>
									<div class="col-xs-5 col-sm-2" ng-class="{loadingInfo:'loading'}">
										<div class="institution-donors" ng-bind="donations.count | number" title="Quantidade de pessoas que fizeram doações a esta Instituição/ONG"></div>
										<div class="institution-donors-caption"><ng-pluralize count="donations.count" when="{'0': 'ajudante :(', 'one': 'ajudante', 'other': 'ajudantes'}"></ng-pluralize></div>
									</div>
									<div class="col-xs-7 col-sm-2" ng-class="{loadingInfo:'loading'}">
										<div class="institution-value" ng-bind="donations.value | currency" title="Valor arrecadado para esta Instituição/ONG"></div>
										<div class="institution-value-caption">arrecadados</div>
									</div>
									<div class="col-xs-12 col-sm-offset-2 col-sm-3 col-md-4">
										<a href="${pageContext.request.contextPath}/${institution.slug}/doar" class="donate-button" title="Siga para a tela de doação">Faça uma Doação</a>
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
						<h1 class="page-title">Saiba Mais ❤</h1>
						<h4 class="page-subtitle">Conheça um pouco do trabalho que você pode ajudar a continuar</h4>
						
						<hr>
						
						${institutionDescriptionMarkdown}
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>