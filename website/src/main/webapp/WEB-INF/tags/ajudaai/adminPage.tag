<%@ tag description="Template Básico das Páginas do Ajuda.Ai" language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" description="Título da Página (opcional)" %>
<%@ attribute name="description" description="Descrição da Página para SEO/Redes Sociais (opcional)" %>
<%@ attribute name="menu" description="Que item de menu será exibido como 'atual'" type="java.lang.Integer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressor" uri="http://htmlcompressor.googlecode.com/taglib/compressor" %>
<compressor:html enabled="true" removeComments="true" preserveLineBreaks="false" removeHttpProtocol="true" removeHttpsProtocol="false" removeJavaScriptProtocol="true" removeMultiSpaces="true" removeIntertagSpaces="true" compressCss="true" compressJavaScript="false">
<!DOCTYPE html>
<html ng-app="ajuda-ai-admin" lang="pt-br">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!</title>
<link rel="icon" type="image/png" href="/res/img/favicon.png" />
<meta name="robots" content="noindex,nofollow" />
<meta name="application-name" content="Ajuda.Ai" />

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/res/css/ajuda-ai-admin.css" rel="stylesheet" type="text/css">

<script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');ga('create','UA-79132344-1','auto');ga('send', 'pageview');</script>
<script type="text/javascript">WebFontConfig = {google:{families:["Lato:300,400,600:latin"]}};</script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.16/webfont.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/markdown.min.js"></script>
<%--script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-animate.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-sanitize.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-touch.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/ui-bootstrap-custom-1.3.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/ui-bootstrap-custom-tpls-1.3.3.js"></script--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/ajuda-ai-admin.js"></script>
<script async defer src="https://buttons.github.io/buttons.js"></script>
</head>
<body>
	<noscript>
		<div id="noscript" class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1>Você está usando o NoScript? Talvez só não curte JavaScript?</h1>
					<p>Sem problemas! Eu <em>(também?)</em> uso o NoScript! É excelente para proteção quando navegamos pela Internet. Garanto que somos seguros, mas se não quiser arriscar, libere apenas nosso domínio <code>https://ajuda.ai/</code> e tudo vai funcionar de boa ;)</p>
				</div>
			</div>
		</div>
	</noscript>
	
	<a href="#content" id="jump-to-content" class="accessibility-link hidden">Ir para o Conteúdo</a>

	<nav class="navbar navbar-default navbar-ajuda-ai navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#main-nav" aria-expanded="false">
					<span class="sr-only">Exibir/Ocultar Navegação</span>
					<span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/admin"><img src="/res/img/main-logo.png" height="45" width="150" alt="Ajuda.Ai" /></a>
			</div>

			<div class="collapse navbar-collapse" id="main-nav">
				<ul class="nav navbar-nav">
					<li<c:if test="${menu == 0}"> class="active"</c:if>><a href="/admin">Resumo <c:if test="${menu == 0}"><span class="sr-only">(você está aqui)</span></c:if></a></li>
					<c:if test="${institution ne null}"><li<c:if test="${menu == 1}"> class="active"</c:if>><a href="/admin/instituicao/${institution.slug}">${institution.name} <c:if test="${menu == 1}"><span class="sr-only">(você está aqui)</span></c:if></a></li></c:if>
				</ul>
				
				<ul class="nav navbar-nav navbar-right">
					<li><a href="/auth/realms/ajuda-ai/account" title="Veja e altere seus dados de Usuário"><strong>${user.name}</strong></a></li>
					<li><a href="/admin/sair" title="Sai da área administrativa e volta ao Ajuda.Ai">Sair</a></li>
				</ul>
			</div>
		</div>
	</nav>

	<div id="content">
		<jsp:doBody></jsp:doBody>
	</div>
	
	<footer id="footer">
		<div class="container">
			<div class="row">
				<div class="col-xs-12 col-sm-6">
					<a href="https://ajuda.ai/" title="Nome do Projeto" class="link-sistema">Sistema Ajuda.Ai</a><small>, por <a href="https://linkedin.com/in/rafaelmadureiralins" title="Sobre o Desenvolvedor do Sistema" target="_blank">Rafael M. Lins</a></small>
				</div>
				<div class="col-xs-12 col-sm-6 text-right github-button">
					<a title="Conheça e contribua com o código do projeto no GitHub" aria-label="Conheça e contribua com o código do projeto no GitHub" data-count-aria-label="# acompanhantes no GitHub" data-count-api="/repos/g0dkar/ajuda-ai#subscribers_count" data-count-href="/g0dkar/ajuda-ai/watchers" href="https://github.com/g0dkar/ajuda-ai" class="github-button" target="_blank">Watch</a>
				</div>
			</div>
		</div>
	</footer>
	
	<a href="https://github.com/g0dkar/ajuda-ai/issues/new" id="bug-found" title="Achou um erro? Reporte ele pra gente!" target="_blank">Achei um erro!</a>
</body>
</html>
</compressor:html>