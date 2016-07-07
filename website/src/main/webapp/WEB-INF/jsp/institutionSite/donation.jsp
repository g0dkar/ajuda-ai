<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:institutionPage institution="${institution}" menu="1">
	<form action="${linkTo[InstitutionSiteController].donate(institution.slug)}" method="post" name="formDonate" class="donation-form clearfix">
		<div class="row donation-value-input">
			<div class="col-xs-12">
				<h2 class="donation-value-title">Qual o <strong>tamanho</strong> da sua Ajuda?</h2>
				
				<div class="row">
					<div class="col-xs-12 col-sm-6 col-md-5 col-lg-4">
						<div class="form-group form-group-lg">
							<div class="input-group">
								<div class="input-group-addon">R$</div>
								<input type="number" class="form-control" id="value" name="value" placeholder="Valor da Contribuição" min="5" max="5000" value="20" autofocus required aria-describedby="value-help-block">
								<div class="input-group-addon">.00</div>
							</div>
							<p class="help-block">Mínimo: $5, Máximo: $5000</p>
							<p class="sr-only" id="value-help-block">Utilize este campo para preencher o valor que você constaria de contribuir</p>
						</div>
					</div>
					<div class="col-xs-12 col-sm-6 col-md-7 col-lg-8">
						<h4 id="donation-desc"></h4>
					</div>
				</div>
				
				<div id="donation-progress">
					<div class="progress">
						<div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="20" aria-valuemin="5" aria-valuemax="60" style="width: 33.3333%">
							<span class="sr-only">Barra demonstrando o valor da doação</span>
						</div>
					</div>
					
					<div class="donation-checkpoint donation-checkpoint-10 active" data-value="10" data-desc="Você é uma pessoa linda por ajudar! (◉ ε ◉)">
						<svg class="donation-heart" viewBox="0 0 32 29.6"><path d="M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.2c6.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z"/></svg>
						<span class="donation-value">$10</span>
					</div>
					
					<div class="donation-checkpoint donation-checkpoint-25 active" data-value="25" data-desc="Cuidado, essa ajuda pode causar um sorrisão (◕ᗜ◕)">
						<svg class="donation-heart" viewBox="0 0 32 29.6"><path d="M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.2c6.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z"/></svg>
						<span class="donation-value">$25</span>
					</div>
					
					<div class="donation-checkpoint donation-checkpoint-50 active" data-value="50" data-desc="Um beijo, um sorrisão e uma gigantesca gratidão! (✧ω✧)">
						<svg class="donation-heart" viewBox="0 0 32 29.6"><path d="M23.6,0c-3.4,0-6.3,2.7-7.6,5.6C14.7,2.7,11.8,0,8.4,0C3.8,0,0,3.8,0,8.4c0,9.4,9.5,11.9,16,21.2c6.1-9.3,16-12.1,16-21.2C32,3.8,28.2,0,23.6,0z"/></svg>
						<span class="donation-value">$50</span>
					</div>
				</div>
			</div>
		</div>
		
		<hr>
		
		<h2>Opções</h2>
		
		<div class="row">
			<div class="col-xs-12 col-sm-6">
				<div class="donation-opt">
					<h3 class="checkbox"><label><input type="checkbox" name="anonymous" id="anonymous" value="1" aria-describedby="anonymous-help-block"> Não publicar meu nome</label></h3>
					<p id="anonymous-help-block" class="help-block">Marcando essa opção, o Ajuda.Ai não irá exibir seu nome e seu e-mail em lugar algum. Ainda assim precisamos de seu nome e e-mail para emitir e enviar seus recibos das doações. <a href="${linkTo[SiteController].page('termos')}">Saiba Mais...</a></p>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6">
				<div class="donation-opt">
					<h3 class="checkbox"><label><input type="checkbox" name="addcosts" id="addcosts" value="1" aria-describedby="addcosts-help-block"> Quero cobrir os custos operacionais</label></h3>
					<div class="form-group">
						<select class="form-control" name="addcoststype" id="addcoststype"><option id="addcosts-cc" value="0">$ - (cartão de crédito)</option><option id="addcosts-others" value="1">$ - (outras formas de pagamento)</option></select>
						<p id="addcosts-help-block" class="help-block">Adicionando esse valor, as taxas cobradas à Instituição/ONG pela Plataforma de Pagamento serão pagas por você. <strong>Nenhum dinheiro</strong> vai para o Ajuda.Ai. <a href="${linkTo[SiteController].page('termos')}">Saiba Mais...</a></p>
					</div>
				</div>
			</div>
		</div>
		
		<hr>
		
		<h2>Seus Dados <small>Para emitir e enviar seus recibos</small></h2>
		
		<div class="row">
			<div class="col-xs-12 col-sm-6">
				<div class="form-group">
					<label for="email" class="control-label">E-mail</label>
					<input type="email" name="email" id="email" class="form-control" placeholder="super.heroi@generosidade.com.br" aria-describedby="email-help-block" required>
					<p class="help-block" id="email-help-block">Quando o recibo desta doação estiver disponível enviaremos para este e-mail.</p>
				</div>
			</div>
			<div class="col-xs-12 col-sm-6">
				<div class="form-group">
					<label for="name" class="control-label">Nome</label>
					<input type="text" name="name" id="name" class="form-control" placeholder="Como se chama essa maravilhosa pessoa?" aria-describedby="name-help-block" required>
					<p id="name-help-block" class="help-block">Precisamos de seu nome para emitir recibos das suas doações. Você receberá um lembrete em seu e-mail assim que o recibo estiver disponível :)</p>
				</div>
			</div>
		</div>
		
		<hr>
		
		<h2>Você é uma Pessoa?</h2>
		<div class="g-recaptcha" data-sitekey="6LcJsSMTAAAAALmEuGm_V1yzF05DGn540TLXd6HH"></div>
		<p class="help-block">Se não for: <a href="https://www.youtube.com/watch?v=KqSIA2ISTvA" target="_blank" title="Klaatu barada nikto!">Gort, Klaatu barada nikto!</a></p>
		
		<hr>
		
		<h2>O último passo será na Plataforma de Pagamento</h2>
		<button type="submit" class="ajuda-ai-btn ajuda-ai-btn-lg" disabled><span class="heart-right">Ok, continuar para lá...</span></button>
		<p style="color:#c00"><strong>Doações Temporariamente Indisponíveis</strong></p>
		<input type="hidden" name="token" id="token" value="${token}">
	</form>
</ajudaai:institutionPage>