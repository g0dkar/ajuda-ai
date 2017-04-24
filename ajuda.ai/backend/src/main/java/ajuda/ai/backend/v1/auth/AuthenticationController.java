package ajuda.ai.backend.v1.auth;

import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import ajuda.ai.backend.v1.ApiController;
import ajuda.ai.backend.v1.user.UserController;
import ajuda.ai.model.user.User;
import ajuda.ai.persistence.model.user.UserPersistence;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.gson.WithoutRoot;
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
	
	@Transactional
	@Post("/login")
	@Consumes(value = { "application/json", "application/x-www-form-urlencoded" }, options = WithoutRoot.class)
	public User login(final User loginUser) {
		final User user = us.getUsernameOrEmail(loginUser.getUsername());
		
		if (user != null) {
			if (loginUser.getPassword() != null && BCrypt.checkpw(loginUser.getPassword(), user.getPassword())) {
				try {
					user.setLastLogin(new Date());
					user.setLastLoginIp(request.getRemoteAddr());
					us.merge(user);
					
					authUser.setUser(user);
					result.forwardTo(UserController.class).me();
				} catch (final Exception e) {
					log.error("Erro ao alterar usuário no seu login", e);
					validator.add(new I18nMessage("error", "authenticationController.login.exceptionUserUpdate"));
				}
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
