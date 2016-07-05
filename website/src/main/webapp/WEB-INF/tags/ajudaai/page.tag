<%@ tag description="Template Básico das Páginas do Ajuda.Ai" language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" description="Título da Página (opcional)" %>
<%@ attribute name="description" description="Descrição da Página para SEO/Redes Sociais (opcional)" %>
<%@ attribute name="menu" description="Qual item de menu estará selecionado? (opcional, integer)" type="java.lang.Integer" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressor" uri="http://htmlcompressor.googlecode.com/taglib/compressor" %>
<compressor:html enabled="true" removeComments="true" preserveLineBreaks="false" removeHttpProtocol="true" removeHttpsProtocol="false" removeJavaScriptProtocol="true" removeMultiSpaces="true" removeIntertagSpaces="true" compressCss="false" compressJavaScript="false">
<!DOCTYPE html>
<html ng-app="ajuda-ai" lang="pt-br">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!</title>
<link rel="icon" type="image/png" href="${cdn}/res/img/favicon.png" />
<%-- for Google --%>
<meta name="robots" content="index,follow" />
<meta name="description" content="<c:out value="${description}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda. Ajude facilmente ONGs e Instituições. Faça a diferença!" />" />
<meta name="keywords" content="ajuda,instituição,ong,diferença,abrigo,associação,beneficente,doação,financiamento coletivo" />
<meta name="author" content="Rafael M. Lins" />
<meta name="copyright" content="Rafael M. Lins, Apache License" />
<meta name="application-name" content="Ajuda.Ai" />

<%-- for Facebook --%>
<meta property="og:title" content="<c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!" />
<meta property="og:description" content="<c:out value="${description}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda. Ajude facilmente ONGs e Instituições. Faça a diferença!" />" />
<meta property="og:type" content="article" />
<meta property="og:image" content="${cdn}/res/img/facebook-preview.jpg" />
<meta property="og:url" content="https://ajuda.ai${pageContext.request.contextPath}/${institution.slug}" />

<%-- for Twitter --%>
<meta name="twitter:card" content="summary" />
<meta name="twitter:title" content="<c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!" />
<meta name="twitter:description" content="<c:out value="${description}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda. Ajude facilmente ONGs e Instituições. Faça a diferença!" />" />
<meta name="twitter:image" content="${cdn}/res/img/twitter-preview.png" />

<%-- Schema.org Metadata --%>
<script type="application/ld+json">{"@context": "http://schema.org","@type": "WebPage","name": "<c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!","description": "<c:out value="${description}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda. Ajude facilmente ONGs e Instituições. Faça a diferença!" />","reviewedBy": "Rafael M. Lins","primaryImageOfPage": "${cdn}/res/img/facebook-preview.jpg","image": "${cdn}/res/img/facebook-preview.jpg","alternativeHeadline": "Juntamos quem quer Ajudar a quem precisa de Ajuda","sameAs": "https://github.com/g0dkar/ajuda-ai","license": "http://www.apache.org/licenses/LICENSE-2.0"}</script>

<link href="https://fonts.googleapis.com/css?family=Josefin+Sans:400,700|Open+Sans:400,700" rel="stylesheet">
<link href="${cdn}/res/css/ajuda-ai.fonts.css" rel="stylesheet" type="text/css">
<link href="${cdn}/res/css/ajuda-ai-v1.min.css" rel="stylesheet" type="text/css">

<script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');ga('create','UA-79132344-1','auto');ga('send', 'pageview');</script>
<style type="text/css">body{background:#f5f3f2;font-family:"Open Sans","Helvetica Neue",Helvetica,Arial,sans-serif}#page-content{opacity:0;transition:opacity 1s ease}#page-content.loaded{opacity:1}#noscript{padding:50px;text-align:center}</style>
</head>
<body>
	<a href="#content" class="sr-only">Ir para o Conteúdo</a>
	<div id="fb-root"></div><script>!function(e,t,n){var o,c=e.getElementsByTagName(t)[0];e.getElementById(n)||(o=e.createElement(t),o.id=n,o.src="//connect.facebook.net/pt_BR/sdk.js#xfbml=1&version=v2.6&appId=1579957135597873",c.parentNode.insertBefore(o,c))}(document,"script","facebook-jssdk");</script>
	
	<div id="page-content">
		<nav class="navbar navbar-fixed-top navbar-default navbar-ajuda-ai">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" aria-expanded="false">
						<span class="sr-only">Exibir Navegação</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					
					<a class="navbar-brand" href="${pageContext.request.contextPath}/" title="Voltar a Página Inicial"><img src="${cdn}/res/img/main-logo.png" height="45" width="150" alt="Ajuda.Ai" /></a>
				</div>
	
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav">
						<li<c:if test="${menu == 1}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/inicio" title="Página Inicial">Início<c:if test="${menu == 1}"> <span class="sr-only">(você está aqui)</span></c:if></a></li>
						<li<c:if test="${menu == 2}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/sobre" title="Conheça um pouco mais sobre a gente">Sobre<c:if test="${menu == 2}"> <span class="sr-only">(você está aqui)</span></c:if></a></li>
						<li<c:if test="${menu == 3}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/contato" title="Gostamos de ouvir, fale com a gente">Contato<c:if test="${menu == 3}"> <span class="sr-only">(você está aqui)</span></c:if></a></li>
						<%--li<c:if test="${menu == 4}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/ajude" title="Quer ajudar o Ajuda.Ai? Veja do que estamos precisando">Ajude-nos!<c:if test="${menu == 4}"> <span class="sr-only">(você está aqui)</span></c:if></a></li--%>
						<%--li<c:if test="${menu == 5}"> class="active"</c:if>><a href="${pageContext.request.contextPath}/minhas-doacoes" title="Verifique e reenvie os recibos de suas doações">Minhas Doações<c:if test="${menu == 5}"> <span class="sr-only">(você está aqui)</span></c:if></a></li--%>
					</ul>
					<%--form class="navbar-form navbar-left" action="${pageContext.request.contextPath}/encontre" role="search">
						<div class="form-group">
							<input type="text" name="q" class="form-control" placeholder="Buscar Instituições/ONGs">
						</div>
						<button type="submit" class="btn btn-default">
							<span class="glyphicon glyphicon-search"></span>
						</button>
					</form--%>
					<ul class="nav navbar-nav navbar-right">
						<li><a href="${pageContext.request.contextPath}/aleatorio" title="Clicando aqui lhe mandaremos aleatoriamente a uma das Instituições/ONGs cadastradas!" class="ajuda-ai-btn-nav"><span class="ajuda-ai-btn">Visitar uma Instituição Aleatória</span></a></li>
						<li><a href="${pageContext.request.contextPath}/admin" title="Acesso Administrativo das Instituições"><c:out value="${user.givenName}" default="Entrar" /></a></li>
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
				
				<hr>
				
				<div class="row">
					<div class="col-xs-6">
						<h4>Parceiros</h4>
						<ul>
							<li><a href="http://cidadeverde.com/genteespecial" target="_blank">Gente Especial</a></li>
							<li><a href="http://cidadeverde.com/" target="_blank">Cidade Verde</a></li>
							<li><a href="http://vikstar.com.br/" target="_blank">Vikstar</a></li>
						</ul>
					</div>
					
					<div class="col-xs-6">
						<h4>O Projeto</h4>
						<ul>
							<li><a href="https://github.com/g0dkar/ajuda-ai" title="Conheça e contribua com o código do projeto no GitHub" target="_blank">GitHub</a></li>
							<li><a href="/contato" title="Fale comigo :)">Contato</a></li>
						</ul>
					</div>
				</div>
			</div>
		</footer>
		
		<a href="https://github.com/g0dkar/ajuda-ai/issues/new" id="bug-found" title="Achou um erro? Reporte ele pra gente!" target="_blank">Achei um erro!</a>
	</div>
	
	<noscript>
		<div id="noscript">
			<h1>Você está usando o NoScript? Talvez só não curte JavaScript?</h1>
			<p>Sem problemas! Eu <em>(também?)</em> uso o NoScript! É excelente para proteção quando navegamos pela Internet. Garanto que somos seguros, mas se não quiser arriscar, esses são os domínios e o que usamos: <code>ajuda.ai</code> (nosso JS), <code>google.com</code> (ReCaptcha), <code>maxcdn.bootstrapcdn.com</code> (CSS do Bootstrap)</p>
		</div>
	</noscript>
	
	<script src="https://buttons.github.io/buttons.js"></script>
	<script src="https://www.google.com/recaptcha/api.js"></script>
	<script src="${cdn}/res/js/ajuda-ai-v1.min.js"></script>
</body>
</html>
</compressor:html>