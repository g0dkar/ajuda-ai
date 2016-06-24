<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ajudaai:adminPage menu="1" title="${institution.name}">
	<div class="content-container">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<h1 class="page-title"><c:out value="${institution.name}" /></h1>
				</div>
			</div>
			
			<div class="row institution-submenu">
				<div class="col-xs-12">
					<ul class="nav nav-pills">
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}" title="Página com informações gerais sobre esta Instituição/ONG">Resumo</a></li>
						<li role="presentation" class="active"><a href="/admin/instituicao/${institution.slug}/posts" title="Gerencie os Posts desta Instituição/ONG">Posts</a></li>
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}/transparencia" title="Informe os gastos desta Instituição/ONG">Transparência</a></li>
						<li role="presentation"><a href="/admin/instituicao/${institution.slug}/detalhes" title="Configure alguns detalhes como logo, banner, nome da instituição, etc.">Detalhes</a></li>
					</ul>
				</div>
			</div>
			
			<c:if test="${errors ne null && !empty(errors)}">
				<div class="row alerts-row">
					<div class="col-xs-12">
						<c:forEach items="${errors}" var="err">
							<div class="alert alert-danger" role="alert"><strong>Erro:</strong> ${err.message}</div>
						</c:forEach>
					</div>
				</div>
			</c:if>
			
			<c:if test="${infoMessage ne null}">
				<div class="row alerts-row">
					<div class="col-xs-12">
						<div class="alert alert-info" role="alert"><strong>Informação:</strong> ${infoMessage}</div>
					</div>
				</div>
			</c:if>
			
			<div class="row">
				<div class="col-xs-12">
					<h2>Posts <small><a href="/admin/instituicao/${institution.slug}/posts/novo">+ Novo post</a></small></h2>
					
					<table class="posts-table table table-striped">
						<thead>
							<tr>
								<th>Post</th>
								<th>Criação</th>
								<th>Visualizações</th>
								<th>Ações</th>
							</tr>
						</thead>
						<tbody>
							<c:choose>
								<c:when test="${posts ne null && !empty(posts)}">
									<c:forEach items="${posts}" var="post">
										<tr class="${post.published ? 'published' : 'unpublished'}">
											<td>
												<a href="/admin/instituicao/${institution.slug}/posts/${post.id}"><c:out value="${post.title}" /><c:if test="${post.published}"> <small>Post não publicado</small></c:if></a>
												<br><a href="https://ajuda.ai/${institution.slug}/${post.slug}/${post.id}" class="post-link">https://ajuda.ai/${institution.slug}/${post.slug}/${post.id}</a>
											</td>
											<td><fmt:formatDate value="${post.creation.time}" pattern="dd/MM/yyyy 'às' HH'h'mm'm'" /></td>
											<td><fmt:formatNumber value="${post.pageviews}" type="number" /> </td>
											<td><a href="/admin/instituicao/${institution.slug}/posts/${post.id}/editar" title="Altere título e conteúdo de seu post">[ Editar ]</a></td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr><td colspan="4"><div class="no-results text-center">Nenhum post... <a href="/admin/instituicao/${institution.slug}/posts/novo" title="Criar um novo post">por enquanto</a> ;)</div></td></tr>
								</c:otherwise>
							</c:choose>
						</tbody>
						<tfoot>
							<tr>
								<th>Post</th>
								<th>Criação</th>
								<th>Visualizações</th>
								<th>Ações</th>
							</tr>
						</tfoot>
					</table>
					
					<p><strong>${postCount}</strong> posts no total.</p>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>