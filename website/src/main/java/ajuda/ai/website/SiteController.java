package ajuda.ai.website;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.model.extra.Page;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.util.mail.SendMail;
import ajuda.ai.website.util.Configuration;
import ajuda.ai.website.util.PersistenceService;
import ajuda.ai.website.util.ReCaptchaService;
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
	private final Configuration conf;
	private final Locale locale;
	private final PersistenceService ps;
	private final SendMail sendmail;
	private final ReCaptchaService recaptcha;
	
	/** @deprecated CDI */ @Deprecated
	SiteController() { this(null, null, null, null, null, null, null, null, null); }
	
	@Inject
	public SiteController(final Logger log, final Result result, final HttpServletRequest request, final Configuration conf, final KeycloakUser user, final Locale locale, final PersistenceService ps, final SendMail sendmail, final ReCaptchaService recaptcha) {
		this.log = log;
		this.result = result;
		this.conf = conf;
		this.locale = locale;
		this.ps = ps;
		this.sendmail = sendmail;
		this.recaptcha = recaptcha;
		
		if (result != null) {
			result.include("user", user);
			result.include("cdn", conf.get("cdn", request.getContextPath()));
		}
	}
	
	@Path("/")
	public void index() {
		final NumberFormat number = NumberFormat.getIntegerInstance(locale);
		final NumberFormat currency = NumberFormat.getNumberInstance(locale);
		currency.setMaximumFractionDigits(2);
		currency.setMinimumFractionDigits(2);
		
		final List<Institution> institutions = ps.createQuery("FROM Institution ORDER BY creation.time DESC").setMaxResults(6).getResultList();
		for (final Institution institution : institutions) {
			institution.getAttributes().put("$$helpers", number.format(((Number) ps.createQuery("SELECT count(*) FROM Helper h JOIN h.institutions i WHERE i = :institution").setParameter("institution", institution).getSingleResult()).longValue()));
			
			final Number donations = (Number) ps.createQuery("SELECT sum(value) FROM Payment WHERE institution = :institution AND paid = true AND cancelled = false").setParameter("institution", institution).getSingleResult();
			institution.getAttributes().put("$$donations", currency.format(donations == null ? 0.0 : (donations.doubleValue() / 100.0)));
		}
		result.include("institutions", institutions);
	}
	
	@Path("/sobre")
	public void about() {
		
	}
	
	@Path("/aleatorio")
	public void random() {
		final String slug = (String) ps.createQuery("SELECT slug FROM Institution ORDER BY RAND()").setMaxResults(1).getSingleResult();
		result.redirectTo(InstitutionSiteController.class).institution(slug);
	}
	
	@Get("/contato")
	public void contact() {
		
	}
	
	@Post("/contato")
	public void contactSend(final String name, final String email, final String content) {
		if (!StringUtils.isBlank(content)) {
			if (recaptcha.isRecaptchaSuccess()) {
				final Map<String, String> templateValues = new HashMap<>(3);
				templateValues.put("name", StringUtils.isBlank(name) ? "Anonymous" : StringUtils.stripHTML(name));
				templateValues.put("email", StringUtils.isBlank(email) ? "Anonymous" : StringUtils.stripHTML(email));
				templateValues.put("content", StringUtils.stripHTML(content).replaceAll("\\n", "<br>"));
				
				sendmail.sendAsync(conf.get("noReplyMail", "no-reply@ajuda.ai"), conf.get("contact.to", "rafael@ajuda.ai"), conf.get("contact.subject", "Contato do Ajuda.Ai"), conf.get("contact.template", "<html><h1>Contato</h1><p>Nome: ${name}</p><p>E-mail: ${email}</p><p>Mensagem: ${content}</p></html>"), templateValues);
				
				result.include("messageContactForm", conf.get("contact.message.success", "Mensagem enviada. Obrigado!"));
			}
			else {
				if (log.isDebugEnabled()) { log.debug("Contact ReCaptcha failed."); }
				result.include("name", name);
				result.include("email", email);
				result.include("content", content);
				result.include("messageContactForm", conf.get("contact.message.captchaFail", "Por favor, faça a verificação se você é mesmo uma pessoa. Por garantia: Gort, Klaatu barada nikto!"));
			}
		}
		else {
			result.include("name", name);
			result.include("email", email);
			result.include("content", content);
			result.include("messageContactForm", conf.get("contact.message.noContent", "Por favor, preencha o Conteúdo :)"));
		}
		
		result.forwardTo(this).contact();
	}
	
	/**
	 * Páginas do site terão mais prioridade que instituições, caso elas queiram maliciosamente sobrescrever uma das páginas.
	 */
	@Path(value = "/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.LOW)
	public void page(final String slug) {
		final Page page = (Page) ps.createQuery("FROM Page WHERE slug = :slug AND published = true").setParameter("slug", slug).getSingleResult();
		
		if (page != null) {
			result.include("page", page);
		}
		else {
			if (log.isDebugEnabled()) { log.debug("Page Not Found: /{} (trying Institution)", slug); }
			result.forwardTo(InstitutionSiteController.class).institution(slug);
		}
	}
}
