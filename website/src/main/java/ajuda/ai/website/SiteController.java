package ajuda.ai.website;

import javax.inject.Inject;

import ajuda.ai.util.keycloak.KeycloakUser;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;

/**
 * Controller principal do Website
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Controller
public class SiteController {
	private final Result result;
	private final KeycloakUser user;
	
	/** @deprecated CDI */ @Deprecated
	SiteController() { this(null, null); }
	
	@Inject
	public SiteController(final Result result, final KeycloakUser user) {
		this.result = result;
		this.user = user;
		
		if (result != null) {
			result.include("user", user);
		}
	}
	
	@Path("/")
	public void index() {
		
	}
	
	@Path("/sobre")
	public void about() {
		
	}
	
	@Path("/contato")
	public void contact() {
		
	}
}
