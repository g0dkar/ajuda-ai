<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div id="page-index" ng-controller="MainController">
		<div class="container-fluid">
			<div class="row">
				<div class="col-xs-12">
					<div class="hero">
						<div class="hero-img">
							<img src="${pageContext.request.contextPath}/res/img/index-img-01.jpg" alt="Juntamos quem quer ajudar a quem precisa de ajuda." />
							<div class="hero-img-text">
								<div class="container">
									<div class="row">
										<div class="col-xs-12">
											<h1>Juntamos quem <strong>quer ajudar</strong> a quem <strong>precisa de ajuda</strong>.</h1>
											<p><a href="${pageContext.request.contextPath}/ama" class="hero-btn">Ajude a AMA - Associação dos Amigos dos Autistas</a></p>
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
						<h1 class="page-title">O Projeto Ajuda.Ai</h1>
						<h4 class="page-subtitle">Que bom que você chegou até aqui! Conheça um pouco sobre nós :)</h4>
						<hr>
						<p>Começamos a partir de uma ideia simples. Um dia alguém me falou sobre fazer um aplicativo para celular para ajudar um pet shop. Por uma feliz coincidência vi um adesivo de uma Associação de Proteção a Animais daqui onde moro e tive a ideia:</p>
						<p>E se tivesse um <a href="https://patreon.com" target="_blank" title="Site que inspirou essa ideia">Patreon</a> onde pudéssemos ajudar não artistas e outras pessoas e projetos mas sim Instituições/ONGs que fazem um trabalho bacana e sempre precisam de ajuda? A partir daí comecei a pensar e trabalhar em cima dessa ideia. Até que um dia, chegou a mim uma fantástica oportunidade.</p>
						<p>Um amigo ligado à Organização de um evento beneficente, o <a href="http://cidadeverde.com/genteespecial" target="_blank" title="Saiba mais sobre o Gente Especial">Gente Especial</a>, organizado pela TV <a href="https://cidadeverde.com/">Cidade Verde</a> (afiliada ao SBT) me perguntou se eu tinha disponibilidade para fazer um sistema para ajudar na arrecadação de recursos para as ONGs beneficiadas e... Bem, uniu a fome à vontade de comer!</p>
						<p>Ele praticamente descreveu minha ideia a mim, e compartilhei com ele minha ideia toda. E aqui estamos. O pontapé inicial do projeto Ajuda.Ai foi dado, graças a todas essas pessoas e a você, que está aqui nos visitando.</p>
						<p><strong>Obrigado!</strong></p>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>