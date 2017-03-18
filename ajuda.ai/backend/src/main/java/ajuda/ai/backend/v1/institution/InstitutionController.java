package ajuda.ai.backend.v1.institution;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.backend.v1.ApiController;
import ajuda.ai.backend.v1.auth.Auth;
import ajuda.ai.backend.v1.auth.AuthenticatedUser;
import ajuda.ai.backend.v1.recaptcha.ReCaptchaService;
import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.payment.PaymentService;
import ajuda.ai.persistence.model.institution.InstitutionPersistence;
import ajuda.ai.util.StringUtils;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;

/**
 * Representa a parte da API relacionada a {@link Institution} e {@link InstitutionPost}.
 * @author Rafael Lins
 *
 */
@Controller
@Path("/institution")
public class InstitutionController extends ApiController {
	private final InstitutionPersistence ip;
	private final PaymentService paymentService;
	private final ReCaptchaService recaptcha;
	private final AuthenticatedUser authUser;
	
	/** @deprecated CDI **/ @Deprecated
	InstitutionController() { this(null, null, null, null, null, null, null, null); }
	
	@Inject
	public InstitutionController(final Logger log, final Result result, final HttpServletRequest request, final Validator validator, final InstitutionPersistence ip, final PaymentService paymentService, final ReCaptchaService recaptcha, final AuthenticatedUser authUser) {
		this.log = log;
		this.result = result;
		this.request = request;
		this.validator = validator;
		
		this.ip = ip;
		this.paymentService = paymentService;
		this.recaptcha = recaptcha;
		this.authUser = authUser;
	}
	
	@Get
	@Path(value = "/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}", priority = Path.LOW)
	public Institution getFromSlug(final String slug) {
		final Institution institution = ip.getSlug(slug);
		
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
		final Institution institution = ip.get(id);
		
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
		final Institution institution = (Institution) ip.query("FROM Institution ORDER BY RAND()").setMaxResults(1).getSingleResult();
		
		if (institution != null) {
			response(institution);
		}
		else {
			result.notFound();
		}
		
		return institution;
	}
	
	@Get("/random-list")
	public List<Institution> randomList(final String amount) {
		final int maxResults = Math.max(1, Math.min(StringUtils.parseInteger(amount, 12), 30));
		final List<Institution> institutions = ip.query("FROM Institution ORDER BY RAND()").setMaxResults(maxResults).getResultList();
		
		if (institutions != null) {
			ip.addHelperCountDonationsValue(institutions);
			response(institutions);
		}
		else {
			result.notFound();
		}
		
		return institutions;
	}
	
	@Get("/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}/donation-stats")
	public int[] donationStats(final String slug) {
		final Institution institution = ip.getSlug(slug);
		final int[] stats = new int[2];
		
		if (institution != null) {
			final Object[] rawStats = ip.getHelperCountDonationsValue(institution);
			
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
		final Institution institution = ip.getSlug(slug);
		List<InstitutionPost> posts = null;
		
		if (institution != null) {
			posts = ip.query("FROM InstitutionPost WHERE institution = :institution AND published = true ORDER BY creation.time DESC").setParameter("institution", institution).getResultList();
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
		final Institution institution = ip.getSlug(slug);
		
		if (institution != null) {
			final InstitutionPost post = (InstitutionPost) ip.query("FROM InstitutionPost WHERE slug = :slug AND institution = :institution AND published = true").setParameter("slug", postSlug).setParameter("institution", institution).getSingleResult();
			
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
	
	@Auth
	@Get("/dashboard-data")
	public InstitutionDashboardData dashboardData() {
		final InstitutionDashboardData data = new InstitutionDashboardData();
		
		final Object[] counts = (Object[]) ip.query("SELECT COUNT(*), SUM(value), COUNT(DISTINCT helper), MAX(value), ROUND(AVG(value), 0) FROM Payment WHERE institution IN (SELECT id FROM Institution WHERE creation.creator = :me) AND cancelled = false AND paid = true").setParameter("me", authUser.get()).getSingleResult();
		
		data.setDonations(counts[0]);
		data.setValue(counts[1]);
		data.setHelpers(counts[2]);
		data.setMaxValue(counts[3]);
		data.setMeanValue(counts[4]);
		
		data.setInstitutionCount(ip.query("SELECT count(*) FROM Institution WHERE creation.creator = :me").setParameter("me", authUser.get()).getSingleResult());
		data.setInstitutions(ip.query("FROM Institution WHERE creation.creator = :me ORDER BY creation.time DESC").setParameter("me", authUser.get()).getResultList());
		
		for (final Institution institution : data.getInstitutions()) {
			institution.getAttributes().put("postsCount", ip.query("SELECT COUNT(*) FROM InstitutionPost WHERE institution = :inst").setParameter("inst", institution).getSingleResult().toString());
		}
		
		response(data);
		
		return data;
	}
	
	@Auth
	@Post("/save")
	@Consumes(value = { "application/json", "application/x-www-form-urlencoded" }, options = WithoutRoot.class)
	@Transactional
	public Institution saveInstitution(Institution institution) {
		final Long institutionId = institution.getId() == null ? 0 : institution.getId();
		CreationInfo creation = (CreationInfo) ip.query("SELECT creation FROM Institution WHERE id = :id").setParameter("id", institutionId).getSingleResult();
		
		if (creation == null) {
			creation = new CreationInfo();
			creation.setTime(new Date());
			creation.setCreator(authUser.get());
		}
		else {
			creation.setLastUpdate(new Date());
			creation.setLastUpdateBy(authUser.get());
		}
		
		institution.setCreation(creation);
		
		final boolean isSlugAvailable = ((Number) ip.query("SELECT count(*) FROM Institution WHERE slug = :slug AND id <> :id").setParameter("slug", institution.getSlug()).setParameter("id", institutionId).getSingleResult()).intValue() == 0;
		if (!isSlugAvailable) {
			validator.add(new I18nMessage("slug", "institutionController.save.slugUnavailable", institution.getSlug()));
		}
		
		if (!validator.validate(institution).hasErrors()) {
			try {
				if (institution.getId() == null) {
					ip.persist(institution);
				}
				else {
					institution = ip.merge(institution);
				}
			} catch (final Exception e) {
				log.error("Erro ao salvar/alterar Instituição (id = " + institutionId + ")", e);
			}
		}
		
		response(institution);
		
		return institution;
	}
}
