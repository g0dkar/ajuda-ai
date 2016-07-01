package ajuda.ai.website;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.model.extra.Page;
import ajuda.ai.util.JsonUtils;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.util.mail.SendMail;
import ajuda.ai.website.util.Configuration;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;

/**
 * Controller principal do Website
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Controller
public class SiteController {
	private final Logger log;
	private final Result result;
	private final HttpServletRequest request;
	private final Configuration conf;
	private final KeycloakUser user;
	private final PersistenceService ps;
	private final SendMail sendmail;
	
	/** @deprecated CDI */ @Deprecated
	SiteController() { this(null, null, null, null, null, null, null); }
	
	@Inject
	public SiteController(final Logger log, final Result result, final HttpServletRequest request, final Configuration conf, final KeycloakUser user, final PersistenceService ps, final SendMail sendmail) {
		this.log = log;
		this.result = result;
		this.request = request;
		this.conf = conf;
		this.user = user;
		this.ps = ps;
		this.sendmail = sendmail;
		
		if (result != null) {
			result.include("user", user);
		}
	}
	
	@Path("/")
	public void index() {
		result.include("institutions", ps.createQuery("FROM Institution ORDER BY creation.time DESC").setMaxResults(6).getResultList());
		result.include("donors", ps.createQuery("SELECT new Map(email as email, count(*) as helps) FROM InstitutionHelper GROUP BY email").setMaxResults(6).getResultList());
	}
	
	@Path("/sobre")
	public void about() {
		
	}
	
	@Get("/contato")
	public void contact() {
		
	}
	
	@Post("/contato")
	public void contactSend(final String name, final String email, final String content) {
		if (!StringUtils.isBlank(content)) {
			if (isRecaptchaSuccess()) {
				final Map<String, String> templateValues = new HashMap<>(3);
				templateValues.put("name", StringUtils.isBlank(name) ? "Anonymous" : StringUtils.stripHTML(name));
				templateValues.put("email", StringUtils.isBlank(email) ? "Anonymous" : StringUtils.stripHTML(email));
				templateValues.put("content", StringUtils.stripHTML(content).replaceAll("\\R", "<br>"));
				
				sendmail.sendAsync(conf.get("noReplyMail", "no-reply@ajuda.ai"), conf.get("contact.to", "rafael@ajuda.ai"), conf.get("contact.subject", "Contato do Ajuda.Ai"), conf.get("contact.template", "<html><h1>Contato</h1><p>Nome: ${name}</p><p>E-mail: ${email}</p><p>Mensagem: ${content}</p></html>"), templateValues).start();
				
				result.include("messageContactForm", conf.get("contact.message.success", "Mensagem enviada. Obrigado!"));
			}
			else {
				result.include("messageContactForm", conf.get("contact.message.captchaFail", "Por favor, faça a verificação se você é mesmo uma pessoa. Por garantia: Gort, Klaatu barada nikto!"));
			}
		}
		else {
			result.include("messageContactForm", conf.get("contact.message.noContent", "Por favor, preencha o Conteúdo :)"));
		}
		
		result.forwardTo(this).contact();
	}
	
	/**
	 * Páginas do site terão mais prioridade que instituições, caso elas queiram maliciosamente sobrescrever uma das páginas.
	 */
	@Path(value = "/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.HIGH)
	public void page(final String slug) {
		final Page page = (Page) ps.createQuery("FROM Page WHERE slug = :slug AND published = true").setParameter("slug", slug).getSingleResult();
		
		if (page != null) {
			result.include("page", page);
		}
		else {
			result.forwardTo(InstitutionSiteController.class).institution(slug);
		}
	}
	
	/**
	 * Verifica um desafio captcha junto ao servidor do ReCaptcha
	 * @return Resposta do servidor (convertida de JSON para Map)
	 */
	private Map<String, Object> getRecaptchaResponse() {
		try {
			final String captchaResponse = request.getParameter("g-recaptcha-response");
			
			if (captchaResponse != null) {
				final StringBuilder payload = new StringBuilder("secret=");
				payload.append(conf.get("recaptcha.secret", "secret"));
				payload.append("&response=");
				payload.append(captchaResponse);
				
				if (log.isDebugEnabled()) { log.debug("Verificando ReCaptcha. Payload: {}", payload); }
				
				final URL url = new URL("https://www.google.com/recaptcha/api/siteverify");
				final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.getOutputStream().write(payload.toString().getBytes());
				
				String line;
				final StringBuilder response = new StringBuilder();
				final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
				
				if (log.isDebugEnabled()) { log.debug("Resposta recebida do ReCaptcha (sem quebras de linha): {}", response); }
				
				return JsonUtils.fromJson(response.toString(), Map.class);
			}
		} catch (final Exception e) {
			log.error("Erro ao verificar ReCaptcha", e);
		}
		
		return null;
	}
	
	/**
	 * Executa {@link #getRecaptchaResponse() o processo de verificação do desafio captcha} e verifica se a resposta corresponde a um sucesso.
	 * 
	 * @return {@code true} se o desafio foi passado com sucesso, {@code false} em qualquer outro caso (erro, captcha em branco, etc.)
	 */
	private boolean isRecaptchaSuccess() {
		final Map<String, Object> captchaResponse = getRecaptchaResponse();
		
		if (captchaResponse != null && !captchaResponse.isEmpty() && captchaResponse.containsKey("success") && captchaResponse.containsKey("hostname")) {
			final Boolean success = (Boolean) captchaResponse.get("success");
			final String hostname = (String) captchaResponse.get("hostname");
			
			if (log.isDebugEnabled()) { log.debug("Sucesso no Captcha? {}, Hostname: {}", success, hostname); }
			
			return success && hostname.endsWith(conf.get("hostname", "ajuda.ai"));
		}
		
		return false;
	}
}
