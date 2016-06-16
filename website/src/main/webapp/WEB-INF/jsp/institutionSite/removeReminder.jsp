<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div id="page-institution" ng-controller="InstitutionController">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<div class="hero">
						<div class="hero-banner">
							<!--img src="/res/img/ama-header.jpg" class="hero-img"-->
							<img src="http://4.bp.blogspot.com/-vCjZ1ZNuRJw/VmxGLPHls5I/AAAAAAAABWc/yBSGgUqnsmQ/s1600-r/banner_AMA1.jpg" class="hero-img">
						</div>
						
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
						<h1 class="page-title">Não quer receber lembretes?</h1>
						<h4 class="page-subtitle">Ok! Prometo que não mando mais nenhum :D</h4>
						
						<hr>
						
						<p>Não se preocupe, já registramos que você não quer mais receber lembretes. Caso queira recebê-los novamente, é só repetir o processo (clicar em "Faça uma Doação" e pedir para receber o lembrete)</p>
						
						<p>Se não for pedir demais, porque você não quer mais receber lembretes para doar para <strong>${institution.name}</strong>? Trabalhamos sempre para evitar abusos e especialmente para que você não se sinta incomodado(a) :)</p>
						
						<form action="/${institution.slug}/api/remove-reminder-reason" method="post">
							<div class="form-group">
								<select class="form-control" name="reason">
									<option>Não pedi para receber lembretes.</option>
									<option>Já fiz uma doação.</option>
									<option>Não sei de onde esse e-mail veio! O q tacon tesse nu?!</option>
									<option>Não confio nessa coisa de Ajuda.Ai.</option>
									<option>Não gostei do conteúdo do e-mail.</option>
									<option>Acho que recebi e-mails demais.</option>
									<option>Não quero mais ajudar.</option>
									<option>Não quero dizer. Só não quero mais receber esses e-mails.</option>
									<option>Outros (não precisa especificar)</option>
								</select>
								<p class="help-block"><small>Ficou realmente chateado(a)? Quer nos dizer pessoalmente algo? <a href="mailto:rafael@ajuda.ai?subject=Lembretes+do+Ajuda.Ai" title="Fale com o criador do Projeto">Mande um e-mail</a> para mim (é tipo sua vingança hahaha).</small></p>
							</div>
							<div class="form-group">
								<button type="submit" class="btn btn-primary">Por isso que quero parar de receber lembretes, beleza?</button>
								<input type="hidden" name="req" value="${removalRequest.id}" >
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/ng-template" id="donation-modal.html">
		<div class="modal-header">
			<h3 class="modal-title">Doação para <strong><c:out value="${institution.name}" /></strong></h3>
		</div>
		
		<div class="modal-body donate-modal-body">
			<p><strong>Muito obrigado(a)!</strong> Independente do tamanho de sua ajuda, ela é imprescindível para manter Instituições/ONGs funcionando. Você pode ajudar agora mesmo ou mais tarde se preferir :)</p>
			
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-6">
						<div class="donation-option donation-option-now">
							<h3>Doar Agora Mesmo!</h3>
							<p>Bacana! Clique no botão abaixo para continuar ^_^</p>
							<form action="https://pagseguro.uol.com.br/checkout/v2/donation.html?iot=button" method="post">
								<input type="hidden" name="currency" value="BRL" />
								<input type="hidden" name="receiverEmail" value="${institution.paymentServiceDataMap['email']}" />
								<input type="image" src="https://stc.pagseguro.uol.com.br/public/img/botoes/doacoes/209x48-doar-assina.gif" width="209" height="48" name="submit" alt="Doação via PagSeguro" ng-disabled="working" />
							</form>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="donation-option donation-option-later" ng-class="{'donation-option-selected': donationOption == 1}" ng-click="donationOption = 1">
							<h3>Me lembre depois</h3>
							<p>Não pode doar agora? OK! Usando essa opção você receberá <strong>1 e-mail a cada semana (na Segunda) por 1 mês</strong> lhe lembrando de fazer essa Doação (o primeiro vai agora, tudo bem?)</p>
						</div>
					</div>
				</div>
			</div>
			
			<form action="/${institution.slug}/api/doar" method="post" name="formDonate" class="donate-form form-horizontal" ng-show="donationOption == 1" ng-submit="doDonate(formDonate, $event)">
				<div class="form-group">
					<label for="name" class="col-sm-2 control-label">Nome*</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="name" ng-model="donation.name" placeholder="Nome dessa pessoa bonita que fará a doação" tabindex="1" required ng-disabled="working">
					</div>
				</div>
				<div class="form-group">
					<label for="email" class="col-sm-2 control-label">E-mail*</label>
					<div class="col-sm-5">
						<input type="email" class="form-control" id="email" ng-model="donation.email" placeholder="Mandaremos o pedido para cá" tabindex="2" required ng-disabled="working">
					</div>
					
					<label for="phone" class="col-sm-2 control-label">Telefone</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="phone" ng-model="donation.phone" placeholder="ou Celular ;P" tabindex="3" ng-disabled="working">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<button class="btn btn-success" type="submit" ng-disabled="working" tabindex="4">Doar mais Tarde :)</button>
					</div>
				</div>
			</form>
		</div>
		
		<div class="modal-footer">
			<button class="btn btn-warning" type="button" ng-click="close()" ng-disabled="working">Fechar</button>
		</div>
    </script>
    <script type="text/ng-template" id="donation-thanks.html">
		<div class="modal-header">
			<h3 class="modal-title">Doação para <strong><c:out value="${institution.name}" /></strong></h3>
		</div>
		
		<div class="modal-body">
			<p><strong>Muito obrigado(a)!</strong> Você deve receber um e-mail da gente em breve.</p>
			<p>Eu poderia te dar um beijo, sabia? [◉ ε ◉]</p>
			<p><small>P.S.: Sei que sou um Website e websites não dão beijos T-T, mas você é uma pessoa linda por querer ajudar ❤</small></p>
		</div>
		
		<div class="modal-footer">
			<button class="btn btn-success" type="button" ng-click="close()">Obrigado *-*</button>
		</div>
	</script>
</ajudaai:page>