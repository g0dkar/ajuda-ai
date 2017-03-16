package ajuda.ai.backend.v1.user;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.backend.v1.ApiController;
import ajuda.ai.backend.v1.auth.Auth;
import ajuda.ai.backend.v1.auth.AuthenticatedUser;
import ajuda.ai.model.user.User;
import ajuda.ai.persistence.model.user.UserPersistence;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;

/**
 * Gerencia a parte da API relacionada a {@link User}.
 * 
 * @author Rafael Lins
 *
 */
@Controller
@Path("/profile")
public class UserController extends ApiController {
	private final UserPersistence us;
	private final AuthenticatedUser authUser;
	
	/** @deprecated CDI **/ @Deprecated
	UserController() { this(null, null, null, null, null, null); }
	
	@Inject
	public UserController(final Logger log, final Result result, final HttpServletRequest request, final Validator validator, final UserPersistence us, final AuthenticatedUser authUser) {
		this.log = log;
		this.result = result;
		this.request = request;
		this.validator = validator;
		
		this.us = us;
		this.authUser = authUser;
	}
	
	@Auth
	@Get("/me")
	public User me() {
		response(authUser.get());
		return authUser.get();
	}
	
	@Auth
	@Get("/{user:[a-zA-Z0-9.\\-]+}")
	public User profile(final String user) {
		final User foundUser = us.getUsername(user);
		
		if (foundUser != null) {
			response(foundUser);
		}
		else {
			result.notFound();
		}
		
		return authUser.get();
	}
}