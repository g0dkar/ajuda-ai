package ajuda.ai.website.admin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;

@Controller
@Path("/admin/instituicao/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}")
public class InstitutionController extends AdminController {
	/** @deprecated CDI */ @Deprecated
	InstitutionController() { this(null, null, null, null, null, null); }
	
	@Inject
	public InstitutionController(final Logger log, final Result result, final PersistenceService ps, final KeycloakUser user, final HttpServletRequest request, final Validator validator) {
		super(log, result, ps, user, request, validator);
	}
	
	private Institution findInstitution(final String slug) {
		if (user.isRole("admin")) {
			return (Institution) ps.createQuery("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
		}
		else {
			return (Institution) ps.createQuery("FROM Institution WHERE slug = :slug AND creation.creator = :user").setParameter("slug", slug).setParameter("user", user.getId()).getSingleResult();
		}
	}
	
	@Path("/")
	public void institution(final String slug) {
		final Institution institution = findInstitution(slug);
		if (institution != null) {
			result.include("institution", institution);
		}
		else {
			result.include("infoMessage", "Instituição inexistente ou você não é o criador da mesma: /" + slug);
			result.redirectTo(DashboardController.class).dashboard();
		}
	}
	
	@Path("/posts")
	public void posts(final String slug, final String page) {
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			result.include("institution", institution);
			
			final int pageSize = 50;
			final int offset = Math.max(0, StringUtils.parseInteger(page, 0)) * pageSize;
			result.include("posts", ps.createQuery("FROM InstitutionPost WHERE institution = :institution ORDER BY creation.time DESC").setParameter("institution", institution).setFirstResult(offset).setMaxResults(pageSize).getResultList());
		}
		else {
			result.include("infoMessage", "Instituição inexistente ou você não é o criador da mesma: /" + slug);
			result.redirectTo(DashboardController.class).dashboard();
		}
	}
	
	@Get("/posts/novo")
	public void newPost(final String slug) {
		final Institution institution = findInstitution(slug);
		if (institution != null) {
			result.include("institution", institution);
		}
		else {
			result.include("infoMessage", "Instituição inexistente ou você não é o criador da mesma: /" + slug);
			result.redirectTo(DashboardController.class).dashboard();
		}
	}
	
	@Transactional
	@Post("/posts/novo")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void createPost(final String slug, final String title, final String content, final String published) {
		validator.onErrorForwardTo(this).newPost(slug);
		final Institution institution = findInstitution(slug);
		
		if (institution != null) {
			final InstitutionPost post = new InstitutionPost();
			post.setInstitution(institution);
			post.setTitle(title);
			post.setSlug(title != null ? StringUtils.slug(title) : null);
			post.setContent(content);
			post.setPublished(StringUtils.parseBoolean(published, false));
			post.setCreation(new CreationInfo());
			post.getCreation().setCreator(user.getId());
			post.getCreation().setTime(new Date());
			
			if (!validator.validate(post).hasErrors()) {
				try {
					ps.persist(post);
				} catch (final Exception e) {
					log.error("Erro ao criar InstitutionPost", e);
				}
				
				result.redirectTo(this).postDetails(slug, String.valueOf(post.getId()));
			}
		}
		else {
			result.include("infoMessage", "Instituição inexistente ou você não é o criador da mesma: /" + slug);
			result.redirectTo(DashboardController.class).dashboard();
		}
	}
	
	@Path("/posts/{id:\\d+}")
	public void postDetails(final String slug, final String id) {
		final Institution institution = findInstitution(slug);
		if (institution != null) {
			final InstitutionPost post = (InstitutionPost) ps.createQuery("FROM InstitutionPost WHERE institution = :institution AND id = :id").setParameter("institution", institution).setParameter("id", StringUtils.parseLong(id, 0)).getSingleResult();
			
			if (post != null) {
				result.include("institution", institution);
				result.include("post", post);
			}
			else {
				result.include("infoMessage", "Post inexistente ou você não tem acesso a ele");
				result.redirectTo(this).posts(slug, null);
			}
		}
		else {
			result.include("infoMessage", "Instituição inexistente ou você não é o criador da mesma: /" + slug);
			result.redirectTo(DashboardController.class).dashboard();
		}
	}
	
	@Path("/api/dashboard-data")
	public void dashboardData(final String slug, final String month, final String year) {
		final Institution institution = findInstitution(slug);
		if (institution != null) {
			final Calendar intervalStart = Calendar.getInstance();
			final Calendar intervalEnd = Calendar.getInstance();
			final Calendar comparisonStart = Calendar.getInstance();
			final Calendar comparisonEnd = Calendar.getInstance();
			
			final int m = StringUtils.parseInteger(month, 0);
			final int y = StringUtils.parseInteger(year, 0);
			
			if (m > 0) {
				intervalStart.set(Calendar.MONTH, m - 1);
				intervalEnd.set(Calendar.MONTH, m - 1);
				comparisonStart.set(Calendar.MONTH, m - 1);
				comparisonEnd.set(Calendar.MONTH, m - 1);
			}
			
			if (y > 0) {
				intervalStart.set(Calendar.YEAR, y);
				intervalEnd.set(Calendar.YEAR, y);
				comparisonStart.set(Calendar.YEAR, y);
				comparisonEnd.set(Calendar.YEAR, y);
			}
			
			intervalStart.set(Calendar.DATE, 1);
			comparisonStart.set(Calendar.DATE, 1);
			comparisonStart.add(Calendar.MONTH, -1);
			
			intervalEnd.set(Calendar.DATE, 1);
			intervalEnd.add(Calendar.MONTH, 1);
			intervalEnd.add(Calendar.DATE, -1);
			
			comparisonEnd.set(Calendar.DATE, 1);
			comparisonEnd.add(Calendar.MONTH, 1);
			comparisonEnd.add(Calendar.DATE, -1);
			comparisonEnd.add(Calendar.MONTH, -1);
			
			final Map<String, Object> result = new HashMap<>(3);
			result.put("helpers", ps.createQuery("SELECT count(*) FROM InstitutionHelper WHERE institution = :institution").setParameter("institution", institution).getSingleResult());
			result.put("currentData", donationDataMap(intervalStart, intervalEnd, institution));
			result.put("previousData", donationDataMap(intervalStart, intervalEnd, institution));
			jsonResponse(result).serialize();
		}
		else {
			result.notFound();
		}
	}
	
	private Map<String, Object> donationDataMap(final Calendar start, final Calendar end, final Institution institution) {
		final String uid = user.getId();
		final Map<String, Object> data = new HashMap<>(7);
		data.put("donationsPerDate", ps.createQuery("SELECT new Map(DATE(p.timestamp) as date, count(p.id) as donations) FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.cancelled = false AND DATE(p.timestamp) BETWEEN DATE(:start) AND DATE(:end) GROUP BY DATE(p.timestamp) ORDER BY DATE(p.timestamp) ASC").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getResultList());
		data.put("grossDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.cancelled = false AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("paidDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.paid = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("availableDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.readyForAccounting = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("meanDonation", ps.createQuery("SELECT avg(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.paid = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("cancelledDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.cancelled = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("newHelpers", ps.createQuery("SELECT count(*) FROM InstitutionHelper h JOIN h.institution i WHERE i.creation.creator = :creator AND DATE(h.timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		
		return data;
	}
}
