<%@ tag description="Template Básico das Páginas do Ajuda.Ai" language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" description="Título da Página (opcional)" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="compressor" uri="http://htmlcompressor.googlecode.com/taglib/compressor" %>
<compressor:html enabled="true" removeComments="true" preserveLineBreaks="false" removeHttpProtocol="true" removeHttpsProtocol="false" removeJavaScriptProtocol="true" removeMultiSpaces="true" removeIntertagSpaces="true" compressCss="true" compressJavaScript="false">
<!DOCTYPE html>
<html lang="${locale.language}-${locale.country}">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${title}" default="Juntamos quem quer Ajudar a quem precisa de Ajuda" /> - Ajuda.Ai</title>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/res/css/ajuda-ai.css" rel="stylesheet" type="text/css">

<script>(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');ga('create','UA-79132344-1','auto');ga('send', 'pageview');</script>
<script type="text/javascript">WebFontConfig = {google:{families:["Lato:400,300,900:latin"]}};</script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/webfont/1.6.16/webfont.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.6/angular.min.js"></script>
<script type="text/javascript" src="/res/js/ajuda-ai.js"></script>
</head>
<body>
	<noscript>
		<div id="noscript" class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1>Você está usando o NoScript? Talvez só não curte JavaScript?</h1>
					<p>Sem problemas! Eu (também?) uso o NoScript! É excelente para proteção quando navegamos pela Internet. Garanto que somos seguros, mas se não quiser arriscar, libere apenas nosso domínio <code>https://ajuda.ai/</code> e tudo vai funcionar de boa ;)</p>
				</div>
			</div>
		</div>
	</noscript>
	
	<a href="#conteudo" id="jump-to-content" class="accessibility-link">Ir para o Conteúdo</a>
	
	<nav>
		<div class="nav-div nav-left">
			<a href="/" title="Ajuda.Ai!" id="main-logo"><img src="/res/img/main-logo.png" alt="Ajuda.Ai" /></a>
			<a href="/" title="Ir para a Página Inicial" class="nav-link">Início</a>
			<a href="/sobre" title="Ir para a Página Inicial" class="nav-link">Sobre</a>
		</div>
		
		<div class="nav-div nav-right">
			<a href="/ama" title="Ir para a Página Inicial" class="nav-link">Ajuda.Ai!</a>
			<c:if test="${user eq null}">
				<a href="/admin" title="Ir para a Página Inicial" class="nav-link nav-link-btn">Sua Instituição/ONG</a>
			</c:if>
		</div>
	</nav>
	
	<div id="content">
		<jsp:doBody></jsp:doBody>
	</div>
</body>
</html>
</compressor:html>