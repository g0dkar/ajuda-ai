package ajuda.ai.website.admin;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

@Controller
@Path("/area")
public class AdminAreaController extends AdminController {
	/** @deprecated CDI */ @Deprecated
	AdminAreaController() { this(null, null, null, null, null, null); }
	
	@Inject
	public AdminAreaController(final Logger log, final Result result, final PersistenceService ps, final KeycloakUser user, final HttpServletRequest request, final Validator validator) {
		super(log, result, ps, user, request, validator);
		
		if (user != null && !user.isRole("admin")) {
			throw new IllegalAccessError("Este usuário não tem permissão para acessar nada dessa área do sistema");
		}
	}
	
	@Path("/")
	public void adminArea() {
		
	}
	
	@Get("/institution")
	public void getInstitution(final String id, final String slug, final String start) {
		if (id == null && slug == null) {
			jsonResponse(ps.createQuery("FROM Institution ORDER BY creation.time DESC").setFirstResult(StringUtils.parseInteger(start, 0)).setMaxResults(50).getResultList()).serialize();
		}
		else {
			Institution institution = null;
			
			if (id != null && id.matches("\\d+")) {
				institution = ps.find(Institution.class, StringUtils.parseLong(id, 0));
			}
			else if (slug != null) {
				institution = (Institution) ps.createQuery("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
			}
			
			if (institution != null) {
				jsonResponse(institution).include("creation").serialize();
			}
			else {
				result.notFound();
			}
		}
	}
	
	@Transactional
	@Post("/institution")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void newInstitution(Institution institution, final String paymentservicedata) {
		if (institution != null) {
			final int slugCount = ((Number) ps.createQuery("SELECT count(*) FROM Slug WHERE slug = :slug").setParameter("slug", institution.getSlug()).getSingleResult()).intValue();
			
			if (slugCount != 0) {
				validator.add(new SimpleMessage("error", "Endereço para Instituição/ONG já está em uso."));
			}
			
			if (institution.getPaymentService() != null) {
				institution.getAttributes().putAll(institution.getPaymentService().extractPaymentServiceData(paymentservicedata));
			}
			
			institution.setCreation(new CreationInfo());
			institution.getCreation().setTime(new Date());
			
			if (!validator.validate(institution).hasErrors()) {
				if (institution.getId() == null) {
					ps.persist(institution);
				}
				else {
					institution = ps.merge(institution);
				}
				
				jsonResponse(institution).include("creation").serialize();
			}
		}
		else {
			result.use(Results.http()).sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
	}
}
