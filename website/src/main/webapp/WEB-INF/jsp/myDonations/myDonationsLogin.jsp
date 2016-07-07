<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ajudaai:page menu="4">
	<div class="slider">
		<div><div class="slider-item">
			<div class="slider-img">
				<img src="${cdn}/res/img/banner-my-donations.jpg" alt="Imagem de uma parede esculpida com mãos segurando uma jarra que distribui água">
			</div>
			<div class="slider-title">
				<h1>Minhas Doações</h1>
			</div>
		</div></div>
	</div>
	
	<div class="container content-page">
		<div class="row">
			<div class="col-xs-12">
				<div class="panel panel-default">
					<div class="panel-body">
						<h1 class="page-title">Acesso às Minhas Doações</h1>
						<h4 class="page-subtitle"><span class="label label-info">Dica</span> Não tem senha? Esqueceu dela? Não quer digitar? Você pode fazer login só com seu e-mail ;)</h4>
						
						<hr>
						
						<c:if test="${errors ne null && !empty(errors)}">
							<div class="alert alert-danger">
								<p style="margin-bottom:15px"><strong>Erros</strong> aconteceram:</p>
								<ul style="margin-bottom:0;padding-bottom:0"><c:forEach items="${errors}" var="err"><li>${err.message}</li></c:forEach></ul>
							</div>
						</c:if>
						
						<c:if test="${messageLogin ne null}">
							<div class="alert alert-info"><c:out value="${messageLogin}" /></div>
						</c:if>
						
						<form action="${linkTo[MyDonationsController].doLogin}" method="post" class="form-vertical">
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<div class="form-group">
										<label for="email" class="control-label required-label">E-mail</label>
										<input type="email" name="email" id="email" class="form-control" autofocus required placeholder="Seu e-mail">
									</div>
								</div>
								<div class="col-xs-12 col-sm-6">
									<div style="margin-top:20px"><button type="submit" class="ajuda-ai-btn ajuda-ai-btn-blue" name="ml" value="1">Login via E-mail</button></div>
									<div class="help-block"><small>Use essa opção se não tem ou esqueceu sua senha</small></div>
								</div>
							</div>
							<div class="row">
								<div class="col-xs-12 col-sm-6">
									<div class="form-group">
										<label for="password" class="control-label">Senha</label>
										<input type="password" name="password" id="password" class="form-control" placeholder="Su4_s3nh@">
									</div>
								</div>
								<div class="col-xs-12 col-sm-6">
									<div style="margin-top:20px"><button type="submit" class="ajuda-ai-btn" name="ml" value="0">Login com E-mail e Senha</button></div>
									<div class="help-block"><small>Mais rápido que com o e-mail ;)</small></div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>