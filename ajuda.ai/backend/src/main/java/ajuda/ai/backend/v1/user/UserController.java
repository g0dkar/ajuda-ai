package ajuda.ai.backend.v1.user;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import ajuda.ai.backend.v1.ApiController;
import ajuda.ai.backend.v1.auth.Auth;
import ajuda.ai.backend.v1.auth.AuthenticatedUser;
import ajuda.ai.model.user.User;
import ajuda.ai.persistence.model.user.UserPersistence;
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
import br.com.caelum.vraptor.view.Results;

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
	@Get
	@Path(value = "/{user:[a-zA-Z0-9.\\-]+}", priority = Path.LOW)
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
	
	@Auth
	@Get("/dashboard-data")
	public void dashboardData() {
		final UserDashboardData data = new UserDashboardData();
		
		final Object[] countData = (Object[]) us.query("SELECT COUNT(*), SUM(value), ROUND(AVG(value), 0), COUNT(DISTINCT institution) FROM Payment WHERE helper = :me AND cancelled = false AND paid = true").setParameter("me", authUser.get()).getSingleResult();
		
		data.setDonations(((Number) countData[0]).intValue());
		data.setValue(countData[1] == null ? 0 : ((Number) countData[1]).intValue());
		data.setMeanValue(countData[2] == null ? 0 : ((Number) countData[2]).intValue());
		data.setInstitutions(((Number) countData[3]).intValue());
		
		data.setPosts(us.query("FROM InstitutionPost ip JOIN FETCH ip.institution WHERE ip.institution IN (SELECT DISTINCT institution FROM Payment WHERE helper = :me AND cancelled = false AND paid = true) ORDER BY ip.creation.time DESC").setParameter("me", authUser.get()).setMaxResults(10).getResultList());
		data.setPayments(us.query("FROM Payment p JOIN FETCH p.institution WHERE p.helper = :me AND p.cancelled = false AND p.paid = true ORDER BY p.timestamp DESC").setParameter("me", authUser.get()).setMaxResults(10).getResultList());
		
		serializer(data).include("posts", "posts.institution", "payments", "payments.institution").exclude("posts.institution.description", "payments.institution.description").serialize();
	}
	
	@Auth
	@Post("/save")
	@Consumes(value = { "application/json", "application/x-www-form-urlencoded" }, options = WithoutRoot.class)
	@Transactional
	public void saveProfile(User user) {
		if (user.getId() == authUser.getId()) {
			if (!StringUtils.isBlank(user.getPassword())) {
				if (BCrypt.checkpw(user.getPassword(), authUser.getPassword())) {
					us.setPassword(user, user.getNewPassword());
				}
				else {
					validator.add(new I18nMessage("password", "userController.save.wrongPassword"));
				}
			}
			else {
				user.setPassword(authUser.getPassword());
			}
			
			user.setCreationTime(authUser.get().getCreationTime());
			user.setLastLogin(authUser.get().getLastLogin());
			user.setLastLoginIp(authUser.get().getLastLoginIp());
				
			if (!validator.validate(user).hasErrors()) {
				try {
					user = us.merge(user);
				} catch (final Exception e) {
					log.error("Erro ao salvar usuário #" + user.getId(), e);
					validator.add(new I18nMessage("error", "userController.save.errorSaving"));
				}
			}
			
			response(user);
		}
		else {
			result.use(Results.http()).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}