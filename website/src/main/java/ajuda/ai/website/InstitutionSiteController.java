package ajuda.ai.website;

import javax.inject.Inject;

import ajuda.ai.model.institution.Institution;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;

@Controller
@Path("/{[a-z][a-z0-9\\-]*[a-z0-9]:id}")
public class InstitutionSiteController {
	private final Result result;
	private final KeycloakUser user;
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	InstitutionSiteController() { this(null, null, null); }
	
	@Inject
	public InstitutionSiteController(final Result result, final KeycloakUser user, final PersistenceService ps) {
		this.result = result;
		this.user = user;
		this.ps = ps;
	}
	
	private Institution institution(final String slug) {
		return (Institution) ps.createQuery("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
	}
	
	@Path({ "", "/" })
	public void instituicao(final String slug) {
		
	}
	
	@Path("/obrigado")
	public void obrigado(final String slug) {
		
	}
}
