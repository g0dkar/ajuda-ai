<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div class="slider" data-slick='{"dots":true,"autoplay":true,"autoplaySpeed":25000}'>
		<div><div class="slider-item">
			<div class="slider-img">
				<img src="${pageContext.request.contextPath}/res/img/index-img-01.jpg" alt="Imagem de Pessoas ajudando umas as outras, colhendo e compartilhando alimentos vegetais">
			</div>
			<div class="slider-title">
				<h1>Juntamos quem <strong>quer ajudar</strong> a quem <strong>precisa de ajuda</strong>.</h1>
				<h3>Às vezes precisamos de mais que uma mão amiga</h3>
				<div><a href="${pageContext.request.contextPath}/aleatorio" class="ajuda-ai-btn">Conheça uma das Instituições que você pode Ajudar!</a></div>
			</div>
		</div></div>
		
		<div><div class="slider-item">
			<div class="slider-img">
				<img src="${pageContext.request.contextPath}/res/img/index-img-02.jpg" alt="Imagem de uma área de convivência com várias pequenas salas">
			</div>
			<div class="slider-title">
				<h1>Fazer a diferença está <strong>mais fácil que nunca</strong></h1>
				<h3>Mais simples que uma compra online</h3>
				<div><a href="/aleatorio" class="ajuda-ai-btn">Ajuda.Ai quem precisa ;)</a></div>
			</div>
		</div></div>
		
		<div><div class="slider-item">
			<div class="slider-img">
				<img src="/res/img/index-img-03.jpg" alt="Imagem de duas crianças aparentemente de mesma idade. Uma menina vestida com um macacão infantil rosa e branco, e um menino vestido com blusa branca e jeans azul. O menino ajuda a menina a subir numa elevação.">
			</div>
			<div class="slider-title">
				<h1>Você tem uma <strong>Instituição</strong> que <strong>precisa de ajuda</strong>?</h1>
				<h3>Entre em contato conosco, podemos ajudar :)</h3>
				<div><a href="/contato" class="ajuda-ai-btn">Fale com a gente!</a></div>
			</div>
		</div></div>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<h2 class="home-title text-center">Conheça quem está precisando da <strong>sua ajuda</strong></h2>
			</div>
		</div>
		<div class="row home-institution-list">
			<c:forEach items="${institutions}" var="institution">
				<div class="col-xs-12 col-sm-6 col-lg-4">
					<a href="${pageContext.request.contextPath}/${institution.slug}" class="panel panel-default institution-panel">
						<span class="panel-body">
							<span class="panel-image">
								<img src="<c:out value="${institution.banner}" default="${pageContext.request.contextPath}/res/img/default-banner.jpg" />" alt="Banner da Insituição" class="panel-image-img">
							</span>
							<span class="panel-avatar">
								<img src="<c:out value="${institution.logo}" default="${pageContext.request.contextPath}/res/img/institution-default.jpg" />" alt="Logo da Instituição" class="panel-avatar-img">
							</span>
							<span class="panel-title-bg">
								<span class="panel-title"><c:out value="${institution.name}" /></span>
							</span>
							<span class="panel-subtitle"><strong class="helper-count">-</strong><strong class="donations-count">$-</strong></span>
						</span>
					</a>
				</div>
			</c:forEach>
		</div>
		
		<%--div class="row">
			<div class="col-xs-12">
				<h2 class="home-title home-title-second text-center">Conheça também os maiores <strong>Ajudantes</strong><span class="heart"></span></h2>
			</div>
		</div>
		<div class="row helpers-list">
			<c:forEach items="${donors}" var="donor">
				<div class="col-xs-6 col-sm-4 col-lg-2">
					<a href="#" class="panel panel-default">
						<span class="panel-body">
							<span class="panel-title"><c:out value="${donors.email}" />Rafael M. Lins de Araújo</span>
							<span class="panel-subtitle"><strong>123</strong> ajudas</span>
						</span>
					</a>
				</div>
			</c:forEach>
		</div--%>
	</div>
</ajudaai:page>