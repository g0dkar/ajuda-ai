<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ajudaai:page>
	<div class="institution-header">
		<div class="slider">
			<div><div class="slider-item slider">
				<div class="slider-img">
					<img src="<c:out value="${page.headerImage eq null ? institution.banner : page.headerImage}" default="${cdn}/res/img/institution-banner-default.jpg" />" title="Banner da Instituição/ONG">
				</div>
			</div></div>
		</div>
		
		<div class="institution-data">
			<div class="institution-name">
				<div class="institution-name-inner">
					<div class="container">
						<div class="row">
							<div class="col-sm-offset-3 col-sm-9 col-md-offset-2 col-md-10">
								<h1><strong><c:out value="${institution.name}" /></strong></h1>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="container">
				<div class="row">
					<div class="col-xs-6 col-sm-3 col-md-2 text-center">
						<div class="institution-avatar"><img src="<c:out value="${institution.logo}" default="${cdn}/res/img/institution-default.jpg"></c:out>" width="150" height="150" class="institution-avatar-img"></div>
					</div>
					<div class="col-xs-6 col-sm-9 col-md-10">
						<div class="row institution-data-row" data-s="${institution.slug}">
							<div class="col-sm-4">
								<div class="institution-helpers"><strong class="helper-count">-</strong> ajudantes</div>
							</div>
							<div class="col-sm-4">
								<div class="institution-donations"><strong class="donations-count">$-</strong> arrecadados</div>
							</div>
							<div class="col-sm-4">
								<div class="institution-call-to-action"><a class="ajuda-ai-btn" href="${pageContext.request.contextPath}/${institution.slug}/doar">Quero Ajudar</a></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="container content-page institution-page">
		<div class="row">
			<div class="col-xs-12 col-sm-8">
				<div class="panel panel-default">
					<div class="panel-body">
						<h1 class="page-title"><c:out value="${page.title}" /></h1>
						<c:if test="${page.subtitle ne null}"><h4 class="page-subtitle"><c:out value="${page.subtitle}" /></h4></c:if>
						
						<hr>
						
						${page.contentMarkdown}
						
						<hr>
						
						<div class="page-signature text-right">Publicado em <time datetime="<fmt:formatDate value="${page.creation.time}" pattern="yyyy-MM-dd'T'HH:mm'Z'" timeZone="UTC" />"><fmt:formatDate value="${page.creation.time}" pattern="dd/MM/yyyy HH'h'mm'm'" /></time><c:if test="${page.creation.lastUpdate ne null}">, Última alteração: <time datetime="<fmt:formatDate value="${page.creation.lastUpdate}" pattern="yyyy-MM-dd'T'HH:mm'Z'" timeZone="UTC" />"><fmt:formatDate value="${page.creation.lastUpdate}" pattern="dd/MM/yyyy HH'h'mm'm'" /></time></c:if></div>
					</div>
				</div>
			</div>
			
			<div class="col-sm-4">
				<div class="panel panel-default">
					<div class="panel-body">
						<h3 class="page-title">O que minha ajuda irá fazer?</h3>
						<p><c:out value="${institution.attributes['cause_desc']}" default="Ajudando com muito ou pouco, você estará contribuindo para continuidade deste projeto e tornando o mundo um lugar melhor para quem precisa" /></p>
						
						<c:if test="${institution.attributes['contact'] ne null}">
							<c:set var="contactInfos" value="${institution.attributes['contact'].split('\\s,\\s')}" />
							<hr>
							
							<h3 class="page-title">Informações para Contato</h3>
							${institution.attributeMarkdown('contact')}
						</c:if>
						
						<c:if test="${institution.attributes['website'] ne null || institution.attributes['facebook'] ne null || institution.attributes['twitter'] ne null}">
							<hr>
							
							<h3 class="page-title">Conecte-se!</h3>
							
							<c:if test="${institution.attributes['website'] ne null}">
								<p><a href="${institution.attributes['website']}" class="website-button" target="_blank">Nosso Website</a></p>
							</c:if>
							
							<c:if test="${institution.attributes['twitter'] ne null}">
								<p><a href="https://twitter.com/${institution.attributes['twitter']}" class="twitter-button" target="_blank">Twitter: @${institution.attributes['twitter']}</a></p>
							</c:if>
							
							<c:if test="${institution.attributes['facebook'] ne null}">
								<div class="fb-page" data-href="https://www.facebook.com/${institution.attributes['facebook']}" data-tabs="timeline" data-small-header="false" data-adapt-container-width="true" data-hide-cover="false" data-show-facepile="true"><p cite="https://www.facebook.com/${institution.attributes['facebook']}" class="fb-xfbml-parse-ignore"><a href="https://www.facebook.com/${institution.attributes['facebook']}">Facebook: @${institution.attributes['facebook']}</a></p></div>
							</c:if>
						</c:if>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:page>