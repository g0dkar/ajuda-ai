package ajuda.ai.website;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.website.paymentServices.PagSeguroPayment;
import ajuda.ai.website.paymentServices.PaymentService;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Controller;
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
@Path("/{[a-z][a-z0-9\\-]*[a-z0-9]:slug}")
public class InstitutionSiteController {
	private final Result result;
	private final Validator validator;
	private final PersistenceService ps;
	private final HttpServletRequest request;
	
	/** @deprecated CDI */ @Deprecated
	InstitutionSiteController() { this(null, null, null, null, null); }
	
	@Inject
	public InstitutionSiteController(final Result result, final Validator validator, final KeycloakUser user, final PersistenceService ps, final HttpServletRequest request) {
		this.result = result;
		this.validator = validator;
		this.ps = ps;
		this.request = request;
		
		if (result != null) {
			result.include("user", user);
		}
	}
	
	private Institution findInstitution(final String slug) {
		return (Institution) ps.createQuery("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
	}
	
	@Path({ "", "/" })
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
	
	@Path("/{[a-z][a-z0-9\\-]*[a-z0-9]:post}")
	public void post(final String slug, final String post) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final InstitutionPost institutionPost = (InstitutionPost) ps.createQuery("FROM InstitutionPost WHERE slug = :slug").setParameter("slug", slug + "/" + post).getSingleResult();
			if (institutionPost != null) {
				result.include("institution", institution);
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
	
	@Post("/doar")
	public void donate(final String slug, final String value, final String name, final String email, final String phone) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			InstitutionHelper helper = (InstitutionHelper) ps.createQuery("FROM InstitutionHelper WHERE institution = :institution AND LOWER(email) = LOWER(:email)").setParameter("institution", institution).setParameter("email", email).getSingleResult();
			final int helpValue = StringUtils.parseInteger(value.replaceAll("\\D+", ""), 0);
			
			if (helper == null) {
				helper = new InstitutionHelper();
				helper.setInstitution(institution);
				helper.setName(name);
				helper.setEmail(email);
				helper.setTimestamp(new Date());
				ps.persist(helper);
			}
			
			final PaymentService paymentService;
			
			switch (institution.getPaymentService()) {
				case MOIP: paymentService = null; break;
				case PAG_SEGURO: paymentService = new PagSeguroPayment(); break;
				default: paymentService = null;
			}
			
			if (paymentService != null) {
				final Payment payment = paymentService.createPayment(institution, helper, helpValue, name, email, phone);
				
				if (payment != null) {
					if (!validator.validate(payment).hasErrors()) {
						ps.persist(payment);
						helper.setLastPayment(payment);
						result.use(Results.json()).withoutRoot().from(payment).serialize();
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
			result.notFound();
		}
		
		if (validator.hasErrors()) {
			validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
		}
	}
	
	@Path("/obrigado")
	public void thanks(final String slug) {
		final Institution institution = findInstitution(slug);
		
		final String transactionId = request.getParameter(institution.getPaymentService().getThanksTransactionIdParameter());
		final Payment payment = (Payment) ps.createQuery("SELECT p FROM Payment p JOIN FETCH p.help WHERE p.paymentServiceId = :id").setParameter("id", transactionId).getSingleResult();
		
		if (institution != null && payment != null) {
			result.include("institution", institution);
			result.include("payment", payment);
		}
		else {
			result.include("notFound", true);
			result.redirectTo(SiteController.class).index();
		}
	}
}
