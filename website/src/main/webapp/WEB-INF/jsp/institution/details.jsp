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
					<p>Configure algumas características da sua instituição. Algumas coisas ainda estão sendo desenvolvidas então tente sempre voltar aqui para dar uma olhadinha se há algo novo :)</p>
					
					<form action="/admin/instituicao/${institution.slug}/detalhes" method="post" class="form-horizontal">
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">Nome da Inst./ONG *</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="name" name="name" ng-model="institution.name" placeholder="Nome de sua Instituição/ONG" value="<c:out value="${institution.name}" />" aria-describedby="name-desc" required autofocus>
								<p id="name-desc" class="help-block">Nome que utilizaremos para chamar sua Instituição/ONG no Website e em todo lugar :)</p>
							</div>
						</div>
						
						<div class="form-group" ng-class="{'has-warning has-feedback': unavailableSlug}">
							<label for="slug" class="col-sm-2 control-label">Endereço *</label>
							<div class="col-sm-10">
								<div class="input-group">
									<span class="input-group-addon">https://ajuda.ai/</span>
									<input type="text" class="form-control" id="slug" name="s" ng-model="institution.slug" ng-model-options="{debounce:500}" placeholder="Endereço que você quer no Ajuda.Ai" value="<c:out value="${institution.slug}" />" aria-describedby="slug-desc" required>
								</div>
								<span class="glyphicon glyphicon-warning-sign form-control-feedback" aria-hidden="true" ng-show="unavailableSlug"></span>
								<p id="slug-desc" class="help-block"><span class="label" ng-bind="unavailableSlug ? 'Endereço Indisponível' : 'Endereço Disponível'" ng-class="{'label-danger':unavailableSlug,'label-success':!unavailableSlug}">Endereço Disponível</span></p>
							</div>
						</div>
						
						<div class="form-group">
							<label for="institution-description" class="col-sm-2 control-label">Texto da sua Página *</label>
							<div class="col-sm-10">
								<textarea class="form-control markdown" markdown="content-preview" ng-model="description" ng-model-options="{debounce:500}" name="description" id="institution-description" placeholder="Texto que será mostrado na página inicial da sua Instituição" aria-describedby="desc-desc" required>${institution.description}</textarea>
								<script type="text/javascript">autosize(document.getElementById("institution-description"))</script>
								<p id="desc-desc" class="help-block">Você pode usar <a href="/res/markdown.jsp" target="_blank">Markdown</a>, uma forma fácil de estilizar seu texto. Siga o link para instruções sobre como usá-lo, é bem simples :)</p>
							</div>
						</div>
						
						<hr>
						
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">Logo *</label>
							<div class="col-sm-10">
								<img src="" width="200" height="200" alt="Logo de sua Instituição/ONG" title="Logo que será utilizado no grafismo do Website. Deve ter 200 de altura por 200 de largura.">
								<input type="text" class="form-control" id="name" name="name" ng-model="institution.name" placeholder="Nome de sua Instituição/ONG" value="<c:out value="${institution.name}" />" aria-describedby="name-desc" required autofocus>
								<p id="name-desc" class="help-block">Nome que utilizaremos para chamar sua Instituição/ONG no Website e em todo lugar :)</p>
							</div>
						</div>
						
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">Nome da Inst./ONG *</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" id="name" name="name" ng-model="institution.name" placeholder="Nome de sua Instituição/ONG" value="<c:out value="${institution.name}" />" aria-describedby="name-desc" required autofocus>
								<p id="name-desc" class="help-block">Nome que utilizaremos para chamar sua Instituição/ONG no Website e em todo lugar :)</p>
							</div>
						</div>
						
						<hr>
						
						<div class="form-group">
							<label for="payserv" class="col-sm-2 control-label">Serviço de Pagamento *</label>
							<div class="col-sm-10">
								<select class="form-control" id="payserv" name="payserv" ng-model="institution.paymentService" required aria-describedby="payserv-desc">
									<option value="MOIP">MoIP</option>
								</select>
								<p id="payserv-desc" class="help-block">Por enquanto suportamos apenas o <a href="https://moip.com.br/" target="_blank" title="Conheça o MoIP">MoIP</a>, mas estamos trabalhando para suportar outros serviços de pagamento. Tem uma sugestão ou gostaria que um em particular fosse feito primeiro? <a href="mailto:rafael@ajuda.ai?subject=Implementar+Servico+de+Pagamento">Mande um e-mail</a> para a gente =D</p>
								
								<div class="row">
									<div class="col-xs-12">
										<h4>Opções do Serviço de Pagamento</h4>
										
										<div ng-if="institution.paymentService == 'MOIP'">
											<div class="form-group">
												<label for="payservdata" class="col-sm-2 control-label">E-mail da conta no MoIP *</label>
												<div class="col-sm-10">
													<input type="text" class="form-control" id="payservdata" name="payservdata" placeholder="E-mail que você usou para se cadastrar no MoIP" value="<c:out value="${institution.paymentServiceDataMap['email']}" />" aria-describedby="payservdata-desc" required>
													<p id="payservdata-desc" class="help-block"><span class="label label-warning">Atenção</span> Quando alguém fizer uma doação ela será feita na conta MoIP deste e-mail! Verifique se é mesmo o seu e está correto!</p>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						
						<hr>
						
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-lg btn-success" ng-disable="checkingSlug || unavailableSlug">Aplicar Alterações</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<div class="row new-post-preview">
				<div class="col-xs-12">
					<h2>Previsão <small>Veja como o Texto de sua Página Inicial ficará</small></h2>
					<div id="content-preview">[ Preview ]</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>