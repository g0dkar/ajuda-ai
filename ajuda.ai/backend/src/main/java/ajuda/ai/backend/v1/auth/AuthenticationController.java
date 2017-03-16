package ajuda.ai.backend.v1.auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import ajuda.ai.backend.v1.ApiController;
import ajuda.ai.backend.v1.user.UserController;
import ajuda.ai.model.user.User;
import ajuda.ai.persistence.model.user.UserPersistence;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;

@Controller
@Path("/auth")
public class AuthenticationController extends ApiController {
	private final UserPersistence us;
	private final AuthenticatedUser authUser;
	
	/** @deprecated CDI **/ @Deprecated
	AuthenticationController() { this(null, null, null, null, null, null); }
	
	@Inject
	public AuthenticationController(final Logger log, final Result result, final HttpServletRequest request, final Validator validator, final UserPersistence us, final AuthenticatedUser authUser) {
		this.log = log;
		this.result = result;
		this.request = request;
		this.validator = validator;
		
		this.us = us;
		this.authUser = authUser;
	}
	
	@Post("/login")
	public User login(final String username, final String password) {
		final User user = us.getUsernameOrEmail(username);
		
		if (user != null) {
			if (password != null && BCrypt.checkpw(password, user.getPassword())) {
				authUser.setUser(user);
				result.redirectTo(UserController.class).me();
			}
			else {
				validator.add(new I18nMessage("error", "authenticationController.login.wrongPassword"));
			}
		}
		else {
			validator.add(new I18nMessage("error", "authenticationController.login.userNotFound"));
		}
		
		errorResponse();
		
		return user;
	}
	
	@Get("/logout")
	public boolean logout(final String username, final String password) {
		authUser.setUser(null);
		response();
		return authUser.get() == null;
	}
}
