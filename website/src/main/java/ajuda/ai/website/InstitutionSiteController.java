package ajuda.ai.website;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.model.institution.ReminderRemovalRequest;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.website.paymentServices.PaymentProcessor;
import ajuda.ai.website.paymentServices.PaymentService;
import ajuda.ai.website.util.PersistenceService;
import ajuda.ai.website.util.ReminderMailSender;
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
@Path("/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}")
public class InstitutionSiteController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final HttpServletRequest request;
	private final PaymentService paymentService;
	private final ReminderMailSender reminderMailSender;
	
	/** @deprecated CDI */ @Deprecated
	InstitutionSiteController() { this(null, null, null, null, null, null, null, null); }
	
	@Inject
	public InstitutionSiteController(final Logger log, final Result result, final Validator validator, final KeycloakUser user, final PersistenceService ps, final HttpServletRequest request, final PaymentService paymentService, final ReminderMailSender reminderMailSender) {
		this.log = log;
		this.result = result;
		this.validator = validator;
		this.ps = ps;
		this.request = request;
		this.paymentService = paymentService;
		this.reminderMailSender = reminderMailSender;
		
		if (result != null) {
			result.include("user", user);
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
			result.include("institutionDescriptionMarkdown", StringUtils.markdown(institution.getDescription()));
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
			result.include("institution", institution);
		}
		else {
			result.include("notFound", true);
			result.redirectTo(SiteController.class).index();
		}
	}
	
	@Path(value = "/{post:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.LOW)
	public void post(final String slug, final String post) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final InstitutionPost institutionPost = (InstitutionPost) ps.createQuery("FROM InstitutionPost WHERE slug = :slug").setParameter("slug", slug + "/" + post).getSingleResult();
			if (institutionPost != null) {
				result.include("institution", institution);
				result.include("institutionPost", institutionPost);
				result.include("institutionPostMarkdown", StringUtils.markdown(institutionPost.getContent()));
			}
			else {
				result.include("notFound", true);
				result.redirectTo(SiteController.class).index();
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
	public void donate(final String slug, final String value, final String name, final String email, final String phone) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			InstitutionHelper helper = (InstitutionHelper) ps.createQuery("FROM InstitutionHelper WHERE institution = :institution AND LOWER(email) = LOWER(:email)").setParameter("institution", institution).setParameter("email", email).getSingleResult();
			final int helpValue = StringUtils.parseInteger(value.replaceAll("\\D+", ""), 0) * 100;
			
			if (helper == null) {
				helper = new InstitutionHelper();
				helper.setInstitution(institution);
				helper.setName(StringUtils.stripHTML(name));
				helper.setEmail(StringUtils.stripHTML(email));
				helper.setPhone(phone == null ? null : phone.replaceAll("\\D+", ""));
				helper.setTimestamp(new Date());
				ps.persist(helper);
			}
			
			final PaymentProcessor paymentProcessor = paymentService.get(institution.getPaymentService());
			
			if (paymentProcessor != null) {
				final Payment payment = paymentProcessor.createPayment(institution, helper, helpValue, ps, result, log);
				
				if (payment != null) {
					ps.createQuery("UPDATE InstitutionHelper SET lastPayment = :payment WHERE id = :id").setParameter("payment", payment).setParameter("id", helper.getId()).executeUpdate();
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
			result.notFound();
		}
		
		if (validator.hasErrors()) {
			validator.onErrorRedirectTo(this).institution(slug);
		}
	}
	
	@Transactional
	@Post("/api/lembrar")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void reminder(final String slug, final String name, final String email, final String phone) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final String token = UUID.randomUUID().toString();
			InstitutionHelper helper = (InstitutionHelper) ps.createQuery("FROM InstitutionHelper WHERE institution = :institution AND LOWER(email) = LOWER(:email)").setParameter("institution", institution).setParameter("email", email).getSingleResult();
			
			if (helper == null) {
				helper = new InstitutionHelper();
				helper.setInstitution(institution);
				helper.setName(StringUtils.stripHTML(name));
				helper.setEmail(StringUtils.stripHTML(email));
				helper.setTimestamp(new Date());
				helper.setReminderToken(token);
				helper.setReminderTokenDate(new Date());
				ps.persist(helper);
			}
			else if (helper.getReminderToken() == null) {
				helper.setName(StringUtils.stripHTML(name));
				helper.setReminderToken(token);
				helper.setReminderTokenDate(new Date());
				ps.merge(helper);
			}
			else {
				helper.setReminderTokenDate(new Date());
				ps.merge(helper);
			}
			
			reminderMailSender.sendReminder(helper, true);
			
			result.use(Results.json()).withoutRoot().from(helper);
		}
		else {
			result.notFound();
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
	
	@Path("/obrigado")
	public void thanks(final String slug) {
		final Institution institution = findInstitution(slug);
		
		
		if (institution != null) {
			result.include("institution", institution);
			
			final String transactionId = request.getParameter(institution.getPaymentService().getThanksTransactionIdParameter());
			final Payment payment = (Payment) ps.createQuery("SELECT p FROM Payment p JOIN FETCH p.institutionHelper WHERE p.paymentServiceId = :id").setParameter("id", transactionId).getSingleResult();
			result.include("payment", payment);
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
			final int helpers = ((Number) ps.createQuery("SELECT count(*) FROM InstitutionHelper WHERE institution = :institution").setParameter("institution", institution).getSingleResult()).intValue();
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
	
	@Transactional
	@Path("/api/unsubscribe/{token:[a-f0-9\\-]+}")
	public void removeReminder(final String slug, final String token) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final InstitutionHelper helper = (InstitutionHelper) ps.createQuery("FROM InstitutionHelper WHERE reminderToken = :token").setParameter("token", token).getSingleResult();
			
			if (helper != null) {
				helper.setReminderToken(null);
				helper.setReminderTokenDate(null);
				ps.merge(helper);
				
				final ReminderRemovalRequest removalRequest = new ReminderRemovalRequest();
				removalRequest.setHelper(helper.getId());
				removalRequest.setTimestamp(new Date());
				removalRequest.setToken(token);
				ps.persist(removalRequest);
				
				result.include("institution", institution);
				result.include("removalRequest", removalRequest);
			}
			else {
				result.redirectTo(this).institution(slug);
			}
		}
		else {
			result.notFound();
		}
	}
	
	@Transactional
	@Post("/api/remove-reminder-reason")
	public void removeReminderReason(final String slug, final String req, final String reason) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			if (!StringUtils.isBlank(reason)) {
				final ReminderRemovalRequest removalRequest = ps.find(ReminderRemovalRequest.class, StringUtils.parseLong(req, 0));
				
				if (removalRequest != null) {
					removalRequest.setReason(StringUtils.stripHTML(reason));
					ps.merge(removalRequest);
				}
			}
			
			result.redirectTo(this).institution(slug);
		}
		else {
			result.notFound();
		}
	}
}
