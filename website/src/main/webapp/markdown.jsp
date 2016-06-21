<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="ajudaai" tagdir="/WEB-INF/tags/ajudaai" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ajudaai:adminPage menu="-1" title="Ajuda Markdown">
	<div class="content-container">
		<div class="container">
			<div class="row">
				<div class="col-xs-12">
					<div class="alert alert-info"><strong>Informação:</strong> Texto retirado de <a href="http://forum.techtudo.com.br/ajuda_markdown/" title="Link para o Texto Original">http://forum.techtudo.com.br/ajuda_markdown/</a></div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-xs-12">
					<div id="main-wrapper">
						<div id="heading">
							<h1>Sintaxe do Markdown</h1>
							<p>Este documento descreve algumas das partes mais importantes do Markdown. No entanto, há muito sobre a sintaxe do que é mencionado aqui. Para obter a documentação completa, acesse página <a href="http://daringfireball.net/projects/markdown/syntax" rel="nofollow">Markdown Syntax</a> de John Gruber.</p>
						</div>
				
						<div class="section">
							<h2 class="section-title">Cabeçalhos</h2>
							<div class="section-description">
								Para cabeçalhos de nível superior sublinhar o texto com sinais de igual. Para os cabeçalhos de segundo nível usar traços para sublinhar.
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>Isto é um H1</code><br>
									<code>============= </code>
								</td>
								<td>
									<h1>Isto é um H1</h1>
								</td>
							</tr></tbody></table>
							
							<table class="section-example"><tbody><tr>
								<td>
									<code>Isto é um H2</code><br>
									<code>-------------</code>
								</td>
								<td>
									<h2>Isto é um H2</h2>
								</td>
							 </tr></tbody></table>
				
							<div class="section-description">
								Se você preferir, você pode usar um hash (#) como prefixo dos cabeçalhos. O número de hashes indica o nível do cabeçalho. Por exemplo, um hash único indica um cabeçalho de nível um, enquanto dois hashes indicam um cabeçalho de segundo nível:
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code># Isto é um H1</code>
								</td>
								<td>
									<h1>Isto é um H1</h1>
								</td>
							</tr></tbody></table>
				
							<table class="section-example"><tbody><tr>
								<td>
									<code>## Isto é um H2</code>
								</td>
								<td>
									<h2>Isto é um H2</h2>
								</td>
							</tr></tbody></table>
				
							<table class="section-example"><tbody><tr>
								<td>
									<code>### Isto é um H3</code>
								</td>
								<td>
									<h3>Isto é um H3</h3>
								</td>
							 </tr></tbody></table>
				
							<div class="section-description">
								Qual você escolhe é uma questão de estilo. O que você acha que fica melhor no documento. Em ambos os casos, o final, o documento totalmente formatado, tem a mesma aparência.
							</div>
						</div>
				
						<div class="section">
							<h2 class="section-title">Parágrafos</h2>
							<div class="section-description">
								Parágrafos são cercados por linhas em branco.
							</div>
							<div class="section-example">
								<code>Esse é o parágrafo um.</code>
							</div><br>
							<div class="section-example">
								<code>Esse é o parágrafo dois.</code>
							</div>
						</div>
				
						<div class="section">
							<h2 class="section-title">Links</h2>
							<div class="section-description">
								
								Há duas partes para cada link.
								O primeiro é o próprio texto que o usuário vai ver e é cercado por colchetes.
								O segundo é o endereço da página que você deseja linkar e é cercado por parênteses.
								
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>[texto do link](http://example.com/)</code>
								</td>
								<td>
									<a>texto do link</a>
								</td>
							</tr></tbody></table>
						</div>
				
						<div class="section">
							<h2 class="section-title">Formatação</h2>
							<div class="section-description">
								Para indicar texto em negrito envolva o texto com dois asteriscos (*) ou dois underscores (_):
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>**Isto está em negrito**</code>
								</td>
								<td>
									<strong>Isto está em negrito</strong>
								</td>
							</tr></tbody></table>
							<table class="section-example"><tbody><tr>
								<td>
									<code>__Isto também está em negrito__</code>
								 </td>
								 <td>
									 <strong>Isto também está em negrito</strong>
								 </td>
							 </tr></tbody></table>
				
							<div class="section-description">
								Para indicar texto em itálico envolva o texto com um único asterisco (*) ou underscore (_):
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>*Isto está em itálico*</code>
								</td>
								<td>
									<i>Isto está em itálico</i>
								</td>
							</tr></tbody></table>
							<table class="section-example"><tbody><tr>
								<td>
									<code>_Isto também está em itálico_</code>
								 </td>
								 <td>
									 <i>Isto também está em itálico</i>
								 </td>
							 </tr></tbody></table>
				
							<div class="section-description">
								Para indicar texto em itálico e negrito envolva o texto com três asteriscos (*) ou underscores (_):
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>***Isto está em negrito e itálico***</code>
								</td>
								<td>
									<strong><i>Isto está em negrito e itálico</i></strong>
								</td>
							</tr></tbody></table>
							<table class="section-example"><tbody><tr>
								<td>
									<code>___Isto também está em negrito e itálico___</code>
								</td>
								<td>
									<strong><i>Isto também está em negrito e itálico</i></strong>
								</td>
							</tr></tbody></table>
						</div>
				
				
						<div class="section">
							<h2 class="section-title">Blockquotes</h2>
							<div class="section-description">
								Para criar uma área indentada use 'maior que' (&gt;) antes de cada linha a ser incluída na blockquote.
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>&gt; Isto é parte de uma blockquote.</code><br>
									<code>&gt; Isto é parte da mesma blockquote.</code>
								</td>
								<td>
									<p style="padding-left:15px;">Isto é parte de uma blockquote.<br>Isto é parte da mesma blockquote.</p>
								</td>
							</tr></tbody></table>
				
							<div class="section-description">
								Ao invés de colocá-lo na frente de cada linha para incluir no blockquote você pode colocá-lo no início e no fim da citação com uma quebra de linha.
							</div>
							<table class="section-example"><tbody><tr>
								<td>
									<code>&gt; Isto é parte de uma blockquote.</code><br>
									<code>Isto faz parte da blockquote mesmo sem usar 'maior que'.</code><br><br>
									<code>A linha em branco fecha a blockquote.</code>
								</td>
								<td>
									<p style="padding-left:15px;">Isto é parte de uma blockquote. <br> Isto faz parte da blockquote mesmo sem usar 'maior que'.</p>
									<p>A linha em branco fecha a blockquote.</p>
								</td>
							</tr></tbody></table>
						</div>
				
						<div class="section">
							 <h2 class="section-title">Listas</h2>
							 <div class="section-description">
								 Para criar uma lista numerada em Markdown, prefixe cada item na lista com um número seguido por um ponto e um espaço. O número que você usa não importa.
							 </div>
							 <table class="section-example"><tbody><tr>
								 <td>
									<code>1. Item 1</code><br>
									<code>2. Item 2</code><br>
									<code>3. Item 3</code>
								 </td>
								 <td>
									<ol>
										<li>Item 1</li>
										<li>Item 2</li>
										<li>Item 3</li>
									</ol>
								 </td>
							 </tr></tbody></table>
				
							 <div class="section-description">
								 Para criar uma lista não ordenada, prefixe cada item da lista com um asterisco (*).
							 </div>
							 <table class="section-example"><tbody><tr>
								 <td>
									<code>* Um item da lista</code><br>
									<code>* Outro item da lista</code><br>
									<code>* Um terceiro item da lista</code>
								 </td>
								 <td>
									<ul>
										<li>Um item da lista</li>
										<li>Outro item da lista</li>
										<li>Um terceiro item da lista</li>
									</ul>
								 </td>
							 </tr></tbody></table>
						</div>
						
						<div class="section">
							<h2 class="section-title">Muito mais</h2>
							<div class="section-description">Há muito mais sobre a sintaxe Markdown do que é mencionado aqui. Mas para escritores criativos, essa ajuda abrange uma série de necessidades. Para saber mais do que você jamais quis realmente saber sobre Markdown, <a href="http://daringfireball.net/projects/markdown/syntax" target="_blank" rel="nofollow">vá para a página do Markdown onde tudo começou</a>.</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</ajudaai:adminPage>