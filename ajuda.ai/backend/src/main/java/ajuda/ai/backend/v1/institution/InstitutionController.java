package ajuda.ai.backend.v1.institution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.backend.v1.ApiController;
import ajuda.ai.backend.v1.recaptcha.ReCaptchaService;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.payment.PaymentService;
import ajuda.ai.persistence.model.institution.InstitutionPersistence;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;

/**
 * Representa a parte da API relacionada a {@link Institution} e {@link InstitutionPost}.
 * @author Rafael Lins
 *
 */
@Controller
@Path("/institution")
public class InstitutionController extends ApiController {
	private final InstitutionPersistence ps;
	private final PaymentService paymentService;
	private final ReCaptchaService recaptcha;
	
	/** @deprecated CDI **/ @Deprecated
	InstitutionController() { this(null, null, null, null, null, null, null); }
	
	@Inject
	public InstitutionController(final Logger log, final Result result, final HttpServletRequest request, final Validator validator, final InstitutionPersistence ps, final PaymentService paymentService, final ReCaptchaService recaptcha) {
		this.log = log;
		this.result = result;
		this.request = request;
		this.validator = validator;
		
		this.ps = ps;
		this.paymentService = paymentService;
		this.recaptcha = recaptcha;
	}
	
	@Get
	@Path(value = "/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.LOW)
	public Institution getFromSlug(final String slug) {
		final Institution institution = ps.getSlug(slug);
		
		if (institution != null) {
			response(institution);
		}
		else {
			result.notFound();
		}
		
		return institution;
	}
	
	@Get("/{id:\\d+}")
	public Institution getFromId(final Long id) {
		final Institution institution = ps.get(id);
		
		if (institution != null) {
			response(institution);
		}
		else {
			result.notFound();
		}
		
		return institution;
	}
	
	@Get("/random")
	public Institution random() {
		final Institution institution = (Institution) ps.query("FROM Institution ORDER BY RAND()").setMaxResults(1).getSingleResult();
		
		if (institution != null) {
			response(institution);
		}
		else {
			result.notFound();
		}
		
		return institution;
	}
	
	@Get("/random-list")
	public List<Institution> randomList() {
		final List<Institution> institution = ps.query("FROM Institution ORDER BY RAND()").setMaxResults(10).getResultList();
		
		if (institution != null) {
			response(institution);
		}
		else {
			result.notFound();
		}
		
		return institution;
	}
	
	@Get("/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}/donation-stats")
	public int[] donationStats(final String slug) {
		final Institution institution = ps.getSlug(slug);
		final int[] stats = new int[2];
		
		if (institution != null) {
			final Object[] rawStats = (Object[]) ps.query("SELECT count(*), sum(value) FROM Payment WHERE institution = :institution AND paid = true AND cancelled = false").setParameter("institution", institution).getSingleResult();
			
			stats[0] = ((Number) rawStats[0]).intValue();
			stats[1] = rawStats[1] == null ? 0 : ((Number) rawStats[1]).intValue();
			
			final Map<String, Object> response = new HashMap<>(2);
			response.put("count", stats[0]);
			response.put("value", stats[1]);
			
			response(response);
		}
		else {
			result.notFound();
		}
		
		return stats;
	}
	
	@Get("/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}/posts")
	public List<InstitutionPost> posts(final String slug) {
		final Institution institution = ps.getSlug(slug);
		List<InstitutionPost> posts = null;
		
		if (institution != null) {
			posts = ps.query("FROM InstitutionPost WHERE institution = :institution AND published = true ORDER BY creation.time DESC").setParameter("institution", institution).getResultList();
			serializer(posts).excludeAll().include("id", "slug", "title", "subtitle", "creation", "creation.creator").serialize();
		}
		else {
			result.notFound();
		}
		
		return posts;
	}
	
	@Get
	@Path(value = "/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}/{postSlug:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.LOW)
	public Institution getFromSlug(final String slug, final String postSlug) {
		final Institution institution = ps.getSlug(slug);
		
		if (institution != null) {
			final InstitutionPost post = (InstitutionPost) ps.query("FROM InstitutionPost WHERE slug = :slug AND institution = :institution AND published = true").setParameter("slug", postSlug).setParameter("institution", institution).getSingleResult();
			
			if (post != null) {
				serializer(post).recursive().exclude("institution").serialize();
			}
			else {
				result.notFound();
			}
		}
		else {
			result.notFound();
		}
		
		return institution;
	}
}
