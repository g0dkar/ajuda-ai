package ajuda.ai.backend;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import ajuda.ai.backend.paymentServices.PaymentProcessor;
import ajuda.ai.backend.paymentServices.PaymentService;
import ajuda.ai.backend.util.CacheService;
import ajuda.ai.backend.util.Configuration;
import ajuda.ai.backend.util.PersistenceService;
import ajuda.ai.backend.util.ReCaptchaService;
import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.institution.Helper;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller para as páginas das Instituições
 * 
 * @author Rafael Lins
 *
 */
@Controller
@Path(value = "/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.LOWEST)
public class InstitutionSiteController {
	private final Logger log;
	private final Result result;
	private final Configuration conf;
	private final Validator validator;
	private final PersistenceService ps;
	private final HttpServletRequest request;
	private final PaymentService paymentService;
	private final CacheService cache;
	private final ReCaptchaService recaptcha;
	
	/** @deprecated CDI */ @Deprecated
	InstitutionSiteController() { this(null, null, null, null, null, null, null, null, null, null); }
	
	@Inject
	public InstitutionSiteController(final Logger log, final Result result, final Configuration conf, final Validator validator, final KeycloakUser user, final PersistenceService ps, final HttpServletRequest request, final PaymentService paymentService, final CacheService cache, final ReCaptchaService recaptcha) {
		this.log = log;
		this.result = result;
		this.conf = conf;
		this.validator = validator;
		this.ps = ps;
		this.request = request;
		this.paymentService = paymentService;
		this.cache = cache;
		this.recaptcha = recaptcha;
		
		if (result != null) {
			result.include("user", user);
			result.include("cdn", conf.get("cdn", request.getContextPath()));
		}
	}
	
	private Institution findInstitution(final String slug) {
		return (Institution) ps.createQuery("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
	}
	
	@Path(value = "/", priority = Path.LOW)
	public void institution(final String slug) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			result.include("institution", institution);
		}
		else {
			result.include("notFound", true);
			result.redirectTo(SiteController.class).index();
		}
	}
	
	@Path("/doar")
	public void donation(final String slug) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final String token = UUID.randomUUID().toString();
			cache.getCache().put("institution_" + slug + "_" + token, 0);
			
			result.include("institution", institution);
			result.include("token", token);
		}
		else {
			result.include("notFound", true);
			result.redirectTo(SiteController.class).index();
		}
	}
	
	@Path(value = "/{[a-z][a-z0-9\\-]*[a-z0-9]}/{id:\\d+}", priority = Path.LOW)
	public void post(final String slug, final String id) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final InstitutionPost post = (InstitutionPost) ps.createQuery("FROM InstitutionPost WHERE institution = :institution AND id = :id").setParameter("institution", institution).setParameter("id", StringUtils.parseLong(id, 0)).getSingleResult();
			
			if (post != null) {
				result.include("institution", institution);
				result.include("post", post);
			}
			else {
				result.include("notFound", true);
				result.redirectTo(this).institution(slug);
			}
		}
		else {
			result.include("notFound", true);
			result.redirectTo(SiteController.class).index();
		}
	}
	
	@Transactional
	@Post("/api/doar")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void donate(final String slug, final String value, final String name, final String email, final String anonymous, final String addcosts, final String addcoststype, final String password) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			if (conf.get("ignoreCaptcha", false) || recaptcha.isRecaptchaSuccess()) {
				if (!StringUtils.isBlank(name) && !StringUtils.isBlank(email)) {
					final int helpValue = StringUtils.parseInteger(value.replaceAll("\\D+", ""), 0) * 100;
					
					if (helpValue < 500) {
						Helper helper = (Helper) ps.createQuery("FROM Helper WHERE LOWER(email) = LOWER(:email)").setParameter("email", email).getSingleResult();
						
						if (helper == null) {
							helper = new Helper();
							helper.setInstitutions(new HashSet<>(1));
							helper.getInstitutions().add(institution);
							helper.setName(StringUtils.stripHTML(name));
							helper.setEmail(StringUtils.stripHTML(email));
							helper.setTimestamp(new Date());
							helper.setAnonymous(StringUtils.parseBoolean(anonymous, StringUtils.isBlank(name)));
							helper.setPassword(BCrypt.hashpw(StringUtils.isEmpty(password) ? randomPassword() : password, BCrypt.gensalt(conf.get("bcrypt.rounds", 10))));
							ps.persist(helper);
						}
						else {
							helper.setName(name);
							helper = ps.merge(helper);
						}
						
						final PaymentProcessor paymentProcessor = paymentService.get(institution.getPaymentService());
						
						if (paymentProcessor != null) {
							final Payment payment = paymentProcessor.createPayment(institution, helper, helpValue, StringUtils.parseBoolean(addcosts, false), StringUtils.parseInteger(addcoststype, -1), ps, result, log);
							
							if (payment != null) {
								try {
									ps.createQuery("UPDATE Helper SET score = score + 1 WHERE id = :id").setParameter("id", helper.getId()).executeUpdate();
								} catch (final Exception e) {
									log.error("Erro ao contabilizar score ao Ajudante", e);
								}
							}
							else {
								validator.add(new SimpleMessage("error", "Erro ao criar Ordem de Pagamento"));
							}
						}
						else {
							validator.add(new SimpleMessage("error", "Serviço de Pagamento não é suportado"));
						}
					}
					else {
						validator.add(new SimpleMessage("error", "Sabemos que é chato, mas não permitimos doações de menos de R$ 5"));
					}
				}
				else {
					if (StringUtils.isBlank(name)){
						validator.add(new SimpleMessage("name", "Por favor, preencha seu nome"));
					}
					if (StringUtils.isBlank(email)){
						validator.add(new SimpleMessage("email", "Por favor, preencha seu e-mail"));
					}
				}
			}
			else {
				validator.add(new SimpleMessage("captcha", "Por favor, execute a verificação do ReCaptcha! Se você for mesmo um robô: Gort, Klaatu barada nikto!"));
			}
		}
		else {
			result.notFound();
		}
		
		if (validator.hasErrors()) {
//			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
			validator.onErrorRedirectTo(this).donation(slug);
		}
	}
	
	@Path("/obrigado")
	public void thanks(final String slug) {
		final Institution institution = findInstitution(slug);
		
		
		if (institution != null) {
			result.include("institution", institution);
			
			final String transactionId = request.getParameter(institution.getPaymentService().getThanksTransactionIdParameter());
			if (transactionId != null && transactionId.matches("[a-f0-9]{32}")) {
				final Payment payment = ps.find(Payment.class, transactionId);
				result.include("payment", payment);
			}
		}
		else {
			result.include("notFound", true);
			result.redirectTo(SiteController.class).index();
		}
	}
	
	@Get("/api/info-doacoes")
	public void donationInfo(final String slug) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final int helpers = ((Number) ps.createQuery("SELECT count(*) FROM Helper h JOIN h.institutions i WHERE i = :institution").setParameter("institution", institution).getSingleResult()).intValue();
			final Number value = (Number) ps.createQuery("SELECT sum(value) FROM Payment WHERE institution = :institution AND paid = true AND cancelled = false").setParameter("institution", institution).getSingleResult();
			
			final Map<String, Object> response = new HashMap<>(2);
			response.put("count", helpers);
			response.put("value", value != null ? new BigDecimal(value.longValue()).divide(PaymentProcessor.HUNDRED).doubleValue() : 0.0);
			
			result.use(Results.json()).withoutRoot().from(response).serialize();
		}
		else {
			result.notFound();
		}
	}
	
	@Get("/api/helper")
	public void helperInfo(final String slug, final String e, final String t) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final String cacheKey = "institution_" + slug + "_" + t;
			final Integer value = (Integer) cache.getCache().get(cacheKey);
			if (value != null && value < conf.get("maxHelperChecksPerToken", 25)) {
				final Map<String, String> res = (Map) ps.createQuery("SELECT new Map(name as name, email as email) FROM Helper WHERE LOWER(email) = LOWER(:email)").setParameter("email", e).getSingleResult();
				if (res != null) {
					result.use(Results.json()).withoutRoot().from(res).serialize();
				}
				else {
					result.notFound();
				}
			}
			else {
				result.notFound();
			}
		}
		else {
			result.notFound();
		}
	}
	
	// Order of the keys on my keyboard... If I'll access them randomly, why have them ordered?
	// Numbers are here to make sure they have a chance to show up
	private static final String LETTERS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	private static final String SPECIAL = "0123456789:.^%=-+[]{}~`_ ,<>å¡!²³&ä®";
	/** @return A random 100 characters long string */
	private String randomPassword() {
		return randomPassword(100, true);
	}
	
	public static String randomPassword(final int size, final boolean special) {
		final SecureRandom r = new SecureRandom();
		final StringBuilder pwd = new StringBuilder();
		
		for (int i = 0; i < size; i++) {
			if (special) {
				if (r.nextBoolean()) {
					pwd.append(LETTERS.charAt(r.nextInt(LETTERS.length())));
				}
				else {
					pwd.append(SPECIAL.charAt(r.nextInt(SPECIAL.length())));
				}
			}
			else {
				pwd.append(LETTERS.charAt(r.nextInt(LETTERS.length())));
			}
		}
		
		return pwd.toString();
	}
}
