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
										<div class="col-xs-12 col-sm-offset-3 col-md-offset-2 col-sm-10">
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
										<div class="donate-button" ng-click="donate()">Faça uma Doação</div>
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
						<h1 class="page-title">Faça uma Doação</h1>
						<h4 class="page-subtitle">Você é uma pessoa linda por querer ajudar [◉ ε ◉]</h4>
						
						<hr>
						
						<c:if test="${errors ne null && !empty(errors)}">
							<div class="alert alert-danger">
								<p><strong>Algo de errado não está certo...</strong> Houveram erros ao processar sua doação :(</p>
								<ul>
									<c:forEach items="${errors}" var="err">
										<li data-category="<c:out value="${err.category}"/>"><c:out value="${err.message}" /></li>
									</c:forEach>
								</ul>
							</div>
						</c:if>
						
						<form action="${pageContext.request.contextPath}/${institution.slug}/api/_doar" method="post" name="formDonate" class="donate-form form-horizontal">
							<div class="form-group form-group-lg">
								<label for="name" class="col-sm-2 control-label">Valor *</label>
								<div class="col-sm-3">
									<div class="input-group">
										<div class="input-group-addon">$</div>
										<input type="number" class="form-control" id="value" name="value" placeholder="Valor da Doação" min="1" max="10000" value="10" tabindex="1" autofocus required>
										<div class="input-group-addon">.00</div>
									</div>
								</div>
							</div>
							
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">Nome *</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="name" name="name" placeholder="Nome dessa pessoa bonita que fará a doação, não precisa ser formal" tabindex="2" required>
								</div>
							</div>
							
							<div class="form-group">
								<label for="email" class="col-sm-2 control-label">E-mail *</label>
								<div class="col-sm-5">
									<input type="email" class="form-control" id="email" name="email" placeholder="Seu e-mail é tipo seu CPF da Internet" tabindex="3" required>
								</div>
							</div>
							
							<div class="form-group">
								<label for="phone" class="col-sm-2 control-label">Telefone (opcional)</label>
								<div class="col-sm-3">
									<input type="text" class="form-control" id="phone" name="phone" placeholder="ou Celular ;P" tabindex="4">
								</div>
							</div>
							
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button class="btn btn-success btn-lg" type="button" tabindex="5" disabled="disabled">Ajuda Aí!</button>
									<p><strong style="color:#c00">Estamos <u>testando</u> as doações. Nenhum valor será realmente cobrado.</strong></p>
								</div>
							</div>
						</form>
						
						<hr>
						
						<h2>Como é o processo de doação?</h2>
						<p>Quando você registra que deseja fazer uma doação nós nos comunicamos junto ao sistema de pagamentos e fazemos uma cobrança a você na conta da Instituição/ONG no valor da sua doação.</p>
						<p>Após preencher o formulário acima você será redirecionado ao ambiente seguro de pagamento e poderá continuar sua doação da forma como preferir, ou mesmo desistir se quiser (por favor, não! haha).</p>
						
						<h2>Por que preciso preencher Endereço de Entrega?</h2>
						<p>A maioria dos serviços de pagamento nacionais são especializados em vendas online, não doações. Há opções para doações, mas normalmente elas não permitem que sistemas como o Ajuda.Ai tenham acesso a acompanhar o andamento das doações. Por isso nossa forma de funcionar é fazendo uma venda online, igual uma loja, com o valor que você deseja.</p>
						<p>Está em nossos planos dar ferramentas às Instituições/ONGs para que elas possam recompensar as pessoas que fazem doações com brindes e coisas semelhantes :)</p>
						
						<h2>Meus dados pessoas ficam com quem?</h2>
						<p>O serviço de pagamento poderá lhe pedir vários dados como seu endereço de entrega (a maioria dos serviços de pagamento nacionais são especializados em vendas online, não doações), nome completo, e-mail, telefone, CPF, etc. <strong>Estes dados serão armazenados no serviço de pagamento</strong> e poderão ser vistos pela Instituição/ONG. Apesar de nosso sistema conseguir acessar esses dados, as únicas informações que registramos são as informadas nesta página. Depois disso tudo que usamos para saber que instituição recebeu que doação é seu e-mail, nada mais. Todos os outros dados vinculados aos pagamentos são ignorados por nosso sistema.</p>
						
						<h2>Posso doar mais de uma vez?</h2>
						<p>Claro! Inclusive, se você fizer isso todos nós vamos ser imensamente gratos a você \(✧ω✧)/</p>
						
						<h2>Tenho outra dúvida...</h2>
						<p>Hey, que bom! Estou a disposição para responder suas dúvidas, basta <a href="mailto:rafael@ajuda.ai?subject=Dúvida+sobre+o+Ajuda.Ai">mandar um e-mail</a> e respondo assim que possível ;) <small>(sou uma pessoa, assim como você e todos que você quer ajudar e vou a faculdade e outras coisas então talvez eu demore um pouquinho para responder, ok?)</small></p>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>