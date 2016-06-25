<%@ tag description="Template Básico das Páginas do Ajuda.Ai" language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" description="Título da Página (opcional)" %>
<%@ attribute name="description" description="Descrição da Página para SEO/Redes Sociais (opcional)" %>
<%@ attribute name="slug" description="Endereço da Página (opcional - por exemplo, se a página que você está fazendo é '/exemplo' então o slug é 'exemplo')" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressor" uri="http://htmlcompressor.googlecode.com/taglib/compressor" %>
<compressor:html enabled="true" removeComments="true" preserveLineBreaks="false" removeHttpProtocol="true" removeHttpsProtocol="false" removeJavaScriptProtocol="true" removeMultiSpaces="true" removeIntertagSpaces="true" compressCss="true" compressJavaScript="false">
<!DOCTYPE html>
<html ng-app="ajuda-ai" lang="pt-br">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!</title>
<link rel="icon" type="image/png" href="/res/img/favicon.png" />
<%-- for Google --%>
<meta name="robots" content="index,follow" />
<meta name="description" content="<c:out value="${description}" default="Com o Ajuda.Ai você facilmente ajuda ONGs e Instituições. Ajude e faça a diferença!" />" />
<meta name="keywords" content="ajuda,instituição,ong,diferença,abrigo,associação,beneficente,doação" />
<meta name="author" content="Rafael M. Lins" />
<meta name="copyright" content="Rafael M. Lins, Apache License" />
<meta name="application-name" content="Ajuda.Ai" />

<%-- for Facebook --%>
<meta property="og:title" content="<c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!" />
<meta property="og:description" content="<c:out value="${description}" default="Com o Ajuda.Ai você facilmente ajuda ONGs e Instituições. Ajude e faça a diferença!" />" />
<meta property="og:type" content="article" />
<meta property="og:image" content="https://ajuda.ai${pageContext.request.contextPath}/res/img/facebook-preview.jpg" />
<meta property="og:url" content="https://ajuda.ai${pageContext.request.contextPath}/${institution.slug}" />

<%-- for Twitter --%>
<meta name="twitter:card" content="summary" />
<meta name="twitter:title" content="<c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!" />
<meta name="twitter:description" content="<c:out value="${description}" default="Com o Ajuda.Ai você facilmente ajuda ONGs e Instituições. Ajude e faça a diferença!" />" />
<meta name="twitter:image" content="${pageContext.request.contextPath}/res/img/twitter-preview.png" />

<%-- Schema.org Metadata --%>
<script type="application/ld+json">{"@context": "http://schema.org","@type": "WebPage","name": "<c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai!","description": "<c:out value="${description}" default="Com o Ajuda.Ai você facilmente ajuda ONGs e Instituições. Ajude e faça a diferença!" />","reviewedBy": "Rafael M. Lins","primaryImageOfPage": "https://ajuda.ai${pageContext.request.contextPath}/res/img/facebook-preview.jpg","image": "https://ajuda.ai${pageContext.request.contextPath}/res/img/facebook-preview.jpg","significantLink": "https://ajuda.ai/ama/","alternativeHeadline": "Juntamos quem quer Ajudar a quem precisa de Ajuda","sameAs": "https://github.com/g0dkar/ajuda-ai","license": "http://www.apache.org/licenses/LICENSE-2.0"}</script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/res/css/ajuda-ai.css" rel="stylesheet" type="text/css">

<script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');ga('create','UA-79132344-1','auto');ga('send', 'pageview');</script>
<script type="text/javascript">WebFontConfig = {google:{families:["Josefin+Sans:300,400,600:latin"]}};</script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.16/webfont.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<%--script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-animate.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-sanitize.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular-touch.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/ui-bootstrap-custom-1.3.3.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/ui-bootstrap-custom-tpls-1.3.3.js"></script--%>
<script type="text/javascript" src="${pageContext.request.contextPath}/res/js/ajuda-ai.js"></script>
<script async defer src="https://buttons.github.io/buttons.js"></script>
</head>
<body>
	<a href="#content" id="jump-to-content" class="accessibility-link hidden">Ir para o Conteúdo</a>
	
	<noscript>
		<div id="noscript" class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1>Você está usando o NoScript? Talvez só não curte JavaScript?</h1>
					<p>Sem problemas! Eu <em>(também?)</em> uso o NoScript! É excelente para proteção quando navegamos pela Internet. Garanto que somos seguros, mas se não quiser arriscar, esses são os domínios e o que usamos: <code>ajuda.ai</code> (nosso JS), <code>ajax.googleapis.com</code> (AngularJS e Google Web Fonts), <code>maxcdn.bootstrapcdn.com</code> (CSS do Bootstrap)</p>
				</div>
			</div>
		</div>
	</noscript>
	
	<nav>
		<div class="nav-inner">
			<a href="${pageContext.request.contextPath}/" title="Ajuda.Ai!" id="main-logo"><img src="/res/img/main-logo.png" height="45" width="150" alt="Ajuda.Ai" /></a>
			<a href="${pageContext.request.contextPath}/" title="Ir para a Página Inicial" class="nav-link hidden-xs">Início</a>
			<a href="${pageContext.request.contextPath}/sobre" title="Ir para a Página Inicial" class="nav-link hidden-xs">Sobre</a>
			<a href="${pageContext.request.contextPath}/admin" title="Acesso a área administrativa" class="nav-link hidden-xs" rel="nofollow">Login</a>
			<a href="${pageContext.request.contextPath}/ama" title="Ajude a Associação dos Amigos dos Autistas" class="nav-link nav-link-btn"><span class="nav-link-btn-inner">Ajuda.Ai!</span></a>
		</div>
		
		<%--
		<div class="nav-div nav-right hidden-xs">
			<c:if test="${user eq null}">
				<a href="${pageContext.request.contextPath}/admin" title="Acesso ao Painel de Controle" class="nav-link">Painel de Controle</a>
			</c:if>
			<a href="${pageContext.request.contextPath}/ama" title="Ajude a Associação dos Amigos dos Autistas" class="nav-link nav-link-btn"><span class="nav-link-btn-inner">Ajuda.Ai!</span></a>
		</div>
		--%>
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
				<div class="col-xs-6 col-md-4">
					<h4>Parceiros</h4>
					<ul>
						<li><a href="http://cidadeverde.com/genteespecial" target="_blank">Gente Especial</a></li>
						<li><a href="http://cidadeverde.com/" target="_blank">Cidade Verde</a></li>
						<li><a href="http://vikstar.com.br/" target="_blank">Vikstar</a></li>
					</ul>
				</div>
				
				<div class="col-xs-6 col-md-4">
					<h4>O Projeto</h4>
					<ul>
						<li><a href="https://github.com/g0dkar/ajuda-ai" title="Conheça e contribua com o código do projeto no GitHub" target="_blank">GitHub</a></li>
						<li><a href="mailto:rafael.lins777@gmail.com?subject=Projeto+Ajuda.Ai" title="Fale comigo :)">Contato</a></li>
					</ul>
				</div>
			</div>
		</div>
	</footer>
	
	<a href="https://github.com/g0dkar/ajuda-ai/issues/new" id="bug-found" title="Achou um erro? Reporte ele pra gente!" target="_blank">Achei um erro!</a>
</body>
</html>
</compressor:html>