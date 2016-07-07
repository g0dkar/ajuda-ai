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
						<h1 class="page-title">Doações de <c:out value="${helper.name}" /></h1>
						<h4 class="page-subtitle">Mais uma vez, um enorme obrigado por cada uma delas :)</h4>
						
						<ul class="nav nav-pills" aria-role="menu">
							<li role="presentation" class="active"><a href="${linkTo[MyDonationsController].myDonations}">Minhas Doações <span class="sr-only">(você está aqui)</span></a></li>
							<li role="presentation"><a href="${linkTo[MyDonationsController].myData}">Meus Dados</a></li>
						</ul>
						
						<c:if test="${errors ne null && !empty(errors)}">
							<div class="alert alert-danger" data-x="${errors.getClass().getName()}">
								<p style="margin-bottom:15px"><strong>Erros</strong> aconteceram:</p>
								<ul style="margin-bottom:0;padding-bottom:0"><c:forEach items="${errors}" var="err"><li>${err.message}</li></c:forEach></ul>
							</div>
						</c:if>
						
						<hr>
						
						<c:choose>
							<c:when test="${payments ne null && !empty(payments)}">
								<c:forEach items="${payments}" var="payment">
									<div class="payment-list-item alert alert-${payment.cancelled ? 'danger' : (payment.paid ? (payment.readyForAccounting ? 'success' : 'info') : 'warning')}">
										<p class="lg">Doação de <strong><fmt:formatNumber value="${payment.value / 100}" type="currency" minFractionDigits="2" maxFractionDigits="2" currencySymbol="R$" /></strong> para <a href="${linkTo[InstitutionSiteController].institution(institution.slug)}" title="Visitar a página da Instituição"><c:out value="${payment.institution.name}" /></a> em <fmt:formatDate value="${payment.timestamp}" pattern="dd/MM/yyyy 'às' HH:mm" /></p>
										<p><strong>Valor Pago:</strong> <fmt:formatNumber value="${payment.realValue / 100}" type="currency" minFractionDigits="2" maxFractionDigits="2" currencySymbol="R$" /><c:if test="${payment.value ne payment.realValue}"><small>&nbsp;<span class="label label-warning">Aviso</span> Você optou por cobrir as taxas do serviço de pagamento</small></c:if></p>
										<p><strong>Identificador:</strong> ${payment.id}</p>
										<p><strong>Serviço de Pagamento:</strong> ${payment.paymentService}
										<p><strong>Status:</strong> ${payment.cancelled ? 'Cancelado' : (payment.paid ? (payment.readyForAccounting ? 'Disponível para a Instituição' : 'Pagamento Efetuado') : 'Aguardando Pagamento')}</p>
										<hr>
										<c:choose>
											<c:when test="${payment.cancelled || !payment.paid}"><div><em>Recibo Indisponível devido ao Status da Doação</em></div></c:when>
											<c:otherwise>
												<div><a href="${linkTo[MyDonationsController].reciept(payment.id)}" class="payment-btn-open"><strong>Imprimir Recibo</strong></a> | <a href="${linkTo[MyDonationsController].mailReciept(payment.id)}" class="payment-btn-mail"><strong>Re-enviar Recibo</strong></a> para seu e-mail</div>
											</c:otherwise>
										</c:choose>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<h2>Nenhuma Doação</h2>
								<p>No momento não temos registrada nenhuma doação sua em nosso sistema. Que tal <a href="${pageContext.request.contextPath}/aleatorio" title="Link que leva a uma Instituição escolhida aleatoriamente">conhecer uma instituição</a> e fazer a primeira?</p>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>