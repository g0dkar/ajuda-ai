<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:institutionPage institution="${institution}">
	<h1>Obrigado! ᕕ(ᵔᗜᵔ)ᕗ</h1>
	
	<p>Nós do Ajuda.Ai e com certeza o pessoal do(a) <strong><c:out value="${institution.name}"/></strong> ficamos imensamente gratos pela sua ajuda!</p>
	<p><small>Sério, obrigado mesmo! Fico super feliz quando usam meus sistemas, ainda mais quando a causa é boa (ง⇀‿↼)ง</small></p>
	
	<blockquote>
		<p style="margin-bottom:15px">Só existem dois dias no ano em que você não pode fazer nada por alguém: ontem e amanhã!</p>
		<footer>Dalai Lama</footer>
	</blockquote>
	
	<h2>Próximos passos</h2>
	<p><a href="#" class="facebook-share-link" data-href="https://ajuda.ai${pageContext.request.contextPath}/${institution.slug}" data-quote="Só existem dois dias no ano em que você não pode fazer nada por alguém: ontem e amanhã! - Dalai Lama" data-hashtag="#ajudaai">Compartilhe</a> essa causa e chame mais gente para ajudar! Continue a corrente do bem :)</p>
	<p class="help-block"><span class="label label-warning">Aviso</span> O valor de sua ajuda irá aparecer assim que o MoIP nos confirmar o recebimento do pagamento! Todas as transações feitas através <strong>MoIP</strong> tem <strong>30 dias</strong> para serem contestadas. Caso queira desistir da ajuda, basta <a href="https://www.moip.com.br/MainMenu.do?method=entrar" target="_blank" title="Link para Acesso a conta no MoIP">acessar sua conta no MoIP</a> e executar o processo por lá.</p>
</ajudaai:institutionPage>