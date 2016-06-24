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
					<h2>Editando Post #${post.id}</h2>
					
					<form action="/admin/instituicao/${institution.slug}/posts/${post.id}/editar" method="post" class="form-horizontal">
						<div class="form-group">
							<label for="title" class="col-sm-2 control-label">Título*</label>
							<div class="col-sm-10">
								<input type="text" class="form-control" name="title" id="title" placeholder="Título do seu post" value="<c:out value="${post.title}" />" autofocus required>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-2">
								<div class="checkbox text-right">
									<label><input type="checkbox" name="published" value="1"${post.published ? ' checked' : ''}> <strong>Publicar Post</strong></label>
								</div>
							</div>
							<div class="col-sm-10">
								<p class="form-control-static">Deixe marcado para tornar o Post visível em sua Página</p>
							</div>
						</div>
						
						<div class="form-group">
							<label for="content" class="col-sm-2 control-label">Conteúdo*</label>
							<div class="col-sm-10">
								<textarea class="form-control markdown" markdown="content-preview" ng-model="content" ng-model-options="{debounce:500}" name="content" id="post-content-input" placeholder="Conteúdo. Você pode usar Markdown." required>${post.content}</textarea>
								<script type="text/javascript">autosize(document.getElementById("post-content-input"))</script>
								<p class="help-block">Você pode usar <a href="/res/markdown.jsp" target="_blank">Markdown</a>, uma forma fácil de estilizar seu texto. Siga o link para instruções sobre como usá-lo, é bem simples :)</p>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-lg btn-success">Alterar Post</button>
							</div>
						</div>
					</form>
				</div>
			</div>
			
			<div class="row new-post-preview">
				<div class="col-xs-12">
					<h2>Previsão <small>Veja como seu Post ficará</small></h2>
					<div id="content-preview">[ Preview ]</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>