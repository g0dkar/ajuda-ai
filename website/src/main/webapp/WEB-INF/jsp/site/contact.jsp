<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page menu="3" title="Contato">
	<div class="slider">
		<div><div class="slider-item">
			<div class="slider-img">
				<img src="${cdn}/res/img/index-img-03.jpg" alt="Imagem de Pessoas ajudando umas as outras">
			</div>
			<div class="slider-title">
				<h1>Queremos <strong>ouvir</strong> (na verdade, ler) tudo que você tem a nos dizer</h1>
			</div>
		</div></div>
	</div>
	
	<div class="container about-page content-page">
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<h1 class="page-title">Contato</h1>
						<h4 class="page-subtitle">Queremos saber o que você tem a nos dizer</h4>
						
						<hr>
						
						<c:if test="${messageContactForm ne null}">
							<div class="alert alert-info"><strong>Informação:</strong> <c:out value="${messageContactForm}" /></div>
						</c:if>
						
						<p>Basta preencher esse formulário e vamos receber sua mensagem! Responderemos assim que possível :)</p>
						
						<form action="${pageContext.request.contextPath}/contato" method="post" class="form-horizontal">
							<div class="form-group">
								<label for="name" class="col-sm-2 control-label">Seu Nome</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="name" id="name" placeholder="Como devemos lhe chamar?" aria-describedby="name-help-block" value="<c:out value="${name}" />">
									<p id="name-help-block" class="help-block"><span class="sr-only">Preencha seu nome neste campo. Não precisa ser nome completo, pode até mesmo ser um apelido.</span> Se preferir o anonimato, basta não preencher. Respeitamos bastante isso!</p>
								</div>
							</div>
							<div class="form-group">
								<label for="email" class="col-sm-2 control-label">Seu E-mail</label>
								<div class="col-sm-10">
									<input type="email" class="form-control" name="email" id="email" placeholder="Seu E-mail" aria-describedby="email-help-block" value="<c:out value="${email}" />">
									<p id="email-help-block" class="help-block"><span class="sr-only">Preencha seu e-mail neste campo.</span> <span class="label label-warning">Atenção</span> Só vamos conseguir lhe responder se soubermos seu e-mail. Se não quiser resposta (ou quiser anonimato), não preencha.</p>
								</div>
							</div>
							<div class="form-group">
								<label for="text-content" class="col-sm-2 control-label required-label">O que se passa em sua cabeça, hmm?</label>
								<div class="col-sm-10">
									<textarea class="form-control" name="content" id="text-content" placeholder="Não se acanhe! Pode falar bem e também pode falar mal! Pode falar muito bem também :P" required autofocus aria-describedby="content-help-block"><c:out value="${content}" /></textarea>
									<p id="content-help-block" class="help-block"><span class="sr-only">Neste campo, digite o que você quer nos dizer.</span> Toda sugestão é bem vinda, toda crítica será considerada e se só quiser conversar mesmo nós estamos de ouvidos em pé para todos :)</p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label required-label">Você é Humano(a)?</label>
								<div class="col-sm-10">
									<span class="sr-only">A seguir, uma verificação Captcha. Para evitar robôs malignos rastejando pela internet.</span>
									<div class="g-recaptcha" data-sitekey="6LcJsSMTAAAAALmEuGm_V1yzF05DGn540TLXd6HH"></div>
									<p class="help-block">Se não for: <a href="https://www.youtube.com/watch?v=KqSIA2ISTvA" target="_blank" title="Klaatu barada nikto!">Gort, Klaatu barada nikto!</a></p>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="submit" class="ajuda-ai-btn">Enviar ➜</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>