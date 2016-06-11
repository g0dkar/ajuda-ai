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
										<div class="col-xs-12 col-sm-3 col-md-2 text-center">
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
										<div class="col-xs-12 col-sm-offset-3 col-md-offset-2 col-sm-2" ng-class="{loadingInfo:'loading'}">
											<div class="institution-donors" ng-bind="donations.count | number" title="Quantidade de pessoas que fizeram doações a esta Instituição/ONG"></div>
											<div class="institution-donors-caption"><ng-pluralize count="donations.count" when="{'0': 'ajudante :(', 'one': 'ajudante', 'other': 'ajudantes'}"></ng-pluralize></div>
										</div>
										<div class="col-xs-12 col-sm-2" ng-class="{loadingInfo:'loading'}">
											<div class="institution-value" ng-bind="donations.value | currency" title="Valor arrecadado para esta Instituição/ONG"></div>
											<div class="institution-value-caption">arrecadados</div>
										</div>
										<div class="col-xs-12 col-sm-offset-2 col-sm-3 col-md-4">
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
						<h1 class="page-title">Saiba Mais</h1>
						<h4 class="page-subtitle">Conheça um pouco do trabalho que você pode ajudar a continuar</h4>
						
						<hr>
						
						${institutionDescriptionMarkdown}
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal-header">
		<h3 class="modal-title">Doação a <c:out value="${institution.name}" />!</h3>
	</div>
	<div class="modal-body">
		<p>Muito obrigado(a)! Independente do tamanho de sua ajuda, ela é imprescindível para manter Instituições/ONGs funcionando.</p>
		<div class="form-horizontal">
			<div class="form-group">
				<label for="name">Seu Nome</label>
				<input type="text" class="form-control" id="name" ng-model="donation.name" placeholder="Seu nome :)">
			</div>
			<div class="form-group">
				<label for="email">Seu E-mail</label>
				<input type="email" class="form-control" id="email" ng-model="donation.email" placeholder="Endereço de E-mail que será enviado o Pedido de Pagamento">
			</div>
			<div class="form-group">
				<label for="value">Valor da Doação</label>
				
				<input type="number" class="form-control" id="value" ng-model="donation.value" placeholder="Valor de sua Doação" min="1" max="100000">
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" type="button" ng-click="ok()">OK</button>
		<button class="btn btn-warning" type="button" ng-click="cancel()">Cancel</button>
	</div>
	
	<script type="text/ng-template" id="donation-modal.html">
		
    </script>
</ajudaai:page>